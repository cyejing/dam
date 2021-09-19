package cn.cyejing.dam.core.filter;

import cn.cyejing.dam.common.module.FilterConfig;
import cn.cyejing.dam.core.context.InternalExchange;
import cn.cyejing.dam.core.context.Response;
import cn.cyejing.dam.core.exception.ErrorResolverFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class FilteringHandler {

    private static final FilteringHandler INSTANCE = new FilteringHandler();

    private final List<Filter<?>> globalFilters = new ArrayList<>();
    private final List<Filter<?>> filters = new ArrayList<>();
    private final Map<String, Filter<?>> filterMap;

    public static FilteringHandler getInstance() {
        return INSTANCE;
    }

    private FilteringHandler() {
        ServiceLoader<Filter> serviceloader = ServiceLoader.load(Filter.class);
        for (Filter<?> filter : serviceloader) {
            if (filter.isGlobal()) {
                globalFilters.add(filter);
            }
            filters.add(filter);
            log.info("scan filter:{}", filter);
        }
        this.filterMap = filters.stream().collect(Collectors.toMap(Filter::getNameToLowerCase, Function.identity()));
    }

    public void handler(InternalExchange exchange) {
        Set<FilterConfig> filterConfigs = exchange.getRoute().getFilterConfigs();
        List<Filter> runFilters = new ArrayList<>(globalFilters);

        for (FilterConfig filterConfig : filterConfigs) {
            Filter<?> filter = filterMap.get(filterConfig.getName());
            if (filter != null) {
                runFilters.add(filter);
            }
        }

        if (runFilters.size() > 1) {
            runFilters.sort((o1, o2) -> Integer.compare(o2.getOrder(), o1.getOrder()));
        }

        DefaultFilterChain chain = new DefaultFilterChain(this, exchange, runFilters.toArray(new Filter[0]));
        chain.doFilter();
    }

    public void complete(InternalExchange exchange) {
        ChannelHandlerContext ctx = exchange.getChannelHandlerContext();
        boolean keepAlive = exchange.isKeepAlive();
        exchange.releaseRequest();
        if (exchange.isCompleted()) {
            if (exchange.isError()) {
                Response response = ErrorResolverFactory.resolve(exchange.getError());
                writeResponse(ctx, response, keepAlive);
            } else {
                writeResponse(ctx, exchange.getResponse(), keepAlive);
            }
        } else {
            RuntimeException e = new RuntimeException();
            log.error("exchange not completed!", e);
            Response response = ErrorResolverFactory.resolve(e);
            writeResponse(ctx, response, keepAlive);
        }
    }

    private void writeResponse(ChannelHandlerContext ctx, Response response, boolean isKeepAlive) {
        if (isKeepAlive) {
            ctx.writeAndFlush(response.build()).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.setHeader(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(response.build());
        }
    }
}
