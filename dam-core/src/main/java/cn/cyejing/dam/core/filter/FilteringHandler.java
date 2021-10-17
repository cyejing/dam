package cn.cyejing.dam.core.filter;

import cn.cyejing.dam.common.config.FilterConfig;
import cn.cyejing.dam.core.context.InternalExchange;
import cn.cyejing.dam.core.context.Response;
import cn.cyejing.dam.core.exception.ErrorResolverFactory;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.LastHttpContent;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class FilteringHandler {

    private static final FilteringHandler INSTANCE = new FilteringHandler();

    private final List<Filter<?>> beforeFilters = new ArrayList<>();
    private final List<Filter<?>> afterFilters = new ArrayList<>();
    private final List<Filter<?>> filters = new ArrayList<>();
    private final Map<String, Filter<?>> filterMap;

    public static FilteringHandler getInstance() {
        return INSTANCE;
    }

    private FilteringHandler() {
        ServiceLoader<Filter> serviceloader = ServiceLoader.load(Filter.class);
        for (Filter<?> filter : serviceloader) {
            if (filter instanceof GlobalFilter) {
                if (((GlobalFilter<?>) filter).getOrder() < 0) {
                    beforeFilters.add(filter);
                }else{
                    afterFilters.add(filter);
                }
            }
            filters.add(filter);
            log.info("scan filter: {}", filter);
        }
        this.filterMap = filters.stream().collect(Collectors.toMap(Filter::getNameToLowerCase, Function.identity()));
    }

    public void handler(InternalExchange exchange) {
        Set<FilterConfig> filterConfigs = exchange.getRoute().getFilterConfigs();
        List<Filter> runFilters = new ArrayList<>(beforeFilters);

        for (FilterConfig filterConfig : filterConfigs) {
            Filter<?> filter = filterMap.get(filterConfig.getName());
            if (filter != null) {
                runFilters.add(filter);
            }
        }

        runFilters.addAll(afterFilters);

        DefaultFilterChain chain = new DefaultFilterChain(this, exchange, runFilters.toArray(new Filter[0]));
        chain.doFilter();
    }

    public void complete(InternalExchange exchange) {
        ChannelHandlerContext ctx = exchange.getChannelHandlerContext();
        exchange.releaseRequest();
        if (exchange.isCompleted()) {
            if (exchange.isError()) {
                Response response = ErrorResolverFactory.resolve(exchange.getError());
                writeResponse(ctx, response, exchange);
            } else {
                writeResponse(ctx, exchange.getResponse(), exchange);
            }
        } else {
            RuntimeException e = new RuntimeException();
            log.error("exchange not completed!", e);
            Response response = ErrorResolverFactory.resolve(e);
            writeResponse(ctx, response, exchange);
        }
    }

    private void writeResponse(ChannelHandlerContext ctx, Response response, InternalExchange exchange) {
        if (exchange.getWritten().compareAndSet(false, true)) {
            if (exchange.isKeepAlive()) {
                response.setHeader(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }else{
                response.setHeader(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            }

            ChannelFuture lastContentFuture;
            if (response.getFileRegion() != null) {
                ctx.write(response.build());
                ctx.write(response.getFileRegion(), ctx.newProgressivePromise());
                lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            }else{
                lastContentFuture = ctx.writeAndFlush(response.build());
            }


            if (!exchange.isKeepAlive()) {
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        } else {
            log.error("Repeat write response");
        }
    }
}
