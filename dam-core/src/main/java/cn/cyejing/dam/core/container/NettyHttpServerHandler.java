package cn.cyejing.dam.core.container;

import cn.cyejing.dam.common.config.DefaultDynamicConfig;
import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.common.config.RouteReadonly;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.context.*;
import cn.cyejing.dam.core.exception.DamException;
import cn.cyejing.dam.core.exception.ErrorCode;
import cn.cyejing.dam.core.exception.ErrorResolverFactory;
import cn.cyejing.dam.core.filter.FilteringHandler;
import cn.cyejing.dam.core.selector.SelectorFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;


@Slf4j
@ChannelHandler.Sharable
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    public NettyHttpServerHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {

        if (message instanceof HttpRequest) {
            handlerRequest(ctx, (FullHttpRequest) message);
        } else {
            // never go this way, If so, it must be a bug
            log.error("message type is not HttpRequest: {}", message);
            if (!ReferenceCountUtil.release(message)) {
                log.error("release message fail.");
            }
        }
    }


    public void handlerRequest(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        try {
            DefaultRequest request = new DefaultRequest(fullHttpRequest, (InetSocketAddress) ctx.channel().remoteAddress());
            RouteReadonly route = matchRoute(request);
            InternalExchange exchange = new DefaultExchange(ctx, fullHttpRequest, request, route);
            FilteringHandler.getInstance().handler(exchange);
        } catch (Throwable e) {
            log.error("build exchange or init filterChain fail", e);
            Response response = ErrorResolverFactory.resolve(e);
            ctx.writeAndFlush(response.build()).addListener(ChannelFutureListener.CLOSE);
            if (!ReferenceCountUtil.release(fullHttpRequest)) {
                log.error("release request fail.");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    private RouteReadonly matchRoute(Request request) {
        RouteReadonly defaultRoute = matchRoutes(request, DefaultDynamicConfig.getInstance().getDefaultRoutes());
        if (defaultRoute == null) {
            if (!request.getPath().equals("/") && !request.getPath().equals("/favicon.ico")) {
                log.info("Searching for the default Route does not find the group. Original request：{}", request.getUrl());
            }
            throw new DamException(ErrorCode.NOT_FOUND_ROUTE);
        }
        SortedSet<Route> routes = DefaultDynamicConfig.getInstance().getRoutes(defaultRoute.getGroup());
        List<Route> noDefaultRoutes = routes.stream().filter(route -> !route.isGlobal()).collect(Collectors.toList());
        RouteReadonly route = matchRoutes(request, noDefaultRoutes);
        if (route == null) {
            route = matchRoutes(request, routes.stream().filter(Route::isGlobal).collect(Collectors.toList()));
            if (route == null) {
                log.info("Route information not found. group:{} Original request：{}", defaultRoute.getGroup(), request.getUrl());
                throw new DamException(ErrorCode.NOT_FOUND_ROUTE);
            }
        }
        return route;
    }


    private RouteReadonly matchRoutes(Request request, Collection<Route> routes) {
        for (Route route : routes) {
            try {
                if (route.getExpression().evaluateBoolean(SelectorFactory.buildContext(request))) {
                    if (route.isLoggable()) {
                        log.info("Match to Route information. path:{} route:{}", request.getPath(), JSONUtil.writeValueAsString(route));
                    }
                    return route.toReadonly();
                }
            } catch (Exception e) {
                log.error("Routing condition match error. group:{},routeId:{},expressionStr:{}",
                        route.getGroup(), route.getId(), route.getExpressionStr(), e);
            }
        }
        return null;
    }
}
