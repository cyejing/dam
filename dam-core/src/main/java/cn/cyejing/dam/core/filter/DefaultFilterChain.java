package cn.cyejing.dam.core.filter;

import cn.cyejing.dam.common.module.FilterConfig;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.context.InternalExchange;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DefaultFilterChain implements FilterChain {

    private final InternalExchange exchange;
    private final Filter<Object>[] filters;
    private FilteringHandler handler;
    private int index = 0;

    public DefaultFilterChain(FilteringHandler handler, InternalExchange exchange, Filter<Object>[] filters) {
        this.handler = handler;
        this.exchange = exchange;
        this.filters = filters;
    }

    @Override
    public void doFilter() {
        if (index >= filters.length || exchange.isCompleted()) {
            handler.complete(exchange);
        }

        try {
            Filter<Object> filter = filters[index++];
            FilterConfig filterConfig = exchange.getFilterConfig(filter.getName());
            Object config = null;
            if (filterConfig != null && filter.getConfigClass() != null && StringUtils.isNotEmpty(filterConfig.getParam())) {
                try {
                    config = JSONUtil.parse(filterConfig.getParam(), filter.getConfigClass());
                } catch (Exception e) {
                    log.error("filter {} params parse error:{}", filter.getName(), filterConfig.getParam(), e);
                }
            }
            filter.filter(exchange, config, this);
        } catch (Throwable t) {
            exchange.occurError(t);
        }

        if (exchange.isCompleted()) {
            handler.complete(exchange);
        }
    }
}
