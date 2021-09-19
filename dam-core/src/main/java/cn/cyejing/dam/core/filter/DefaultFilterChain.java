package cn.cyejing.dam.core.filter;

import cn.cyejing.dam.common.module.FilterConfig;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.context.InternalExchange;
import cn.cyejing.dam.core.support.cache.CacheManager;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DefaultFilterChain implements FilterChain {

    private final Cache<String, Object> cache;
    private final InternalExchange exchange;
    private final Filter<Object>[] filters;
    private FilteringHandler handler;
    private int index = 0;

    public DefaultFilterChain(FilteringHandler handler, InternalExchange exchange, Filter<Object>[] filters) {
        this.handler = handler;
        this.exchange = exchange;
        this.filters = filters;
        this.cache = CacheManager.createForRoute(exchange.getRoute().getId());
    }

    @Override
    public void doFilter() {
        if (index >= filters.length || exchange.isCompleted()) {
            handler.complete(exchange);
            return;
        }

        try {
            Filter<Object> filter = filters[index++];
            FilterConfig filterConfig = exchange.getFilterConfig(filter.getNameToLowerCase());
            if (filterConfig == null) {
                filter.filter(this, exchange, null);
            } else {
                Object config = cache.get(filterConfig.getName(), s -> {
                    if (filter.getConfigClass() != null && StringUtils.isNotEmpty(filterConfig.getParam())) {
                        try {
                            return JSONUtil.parse(filterConfig.getParam(), filter.getConfigClass());
                        } catch (Exception e) {
                            log.error("filter {} params parse error:{}", filter.getNameToLowerCase(), filterConfig.getParam(), e);
                        }
                    }
                    return null;
                });

                filter.filter(this, exchange, config);
            }
        } catch (Throwable t) {
            exchange.occurError(t);
        }

        if (exchange.isCompleted()) {
            handler.complete(exchange);
        }
    }
}
