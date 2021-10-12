package cn.cyejing.dam.core.context;

import cn.cyejing.dam.common.config.DefaultDynamicConfig;
import cn.cyejing.dam.common.config.FilterConfig;
import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.common.config.RouteReadonly;
import cn.cyejing.dam.common.constants.Protocol;
import cn.cyejing.dam.common.utils.Assert;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.exception.DamException;
import cn.cyejing.dam.core.exception.ErrorCode;
import cn.cyejing.dam.core.selector.SelectorFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
public class DefaultExchange implements Exchange, InternalExchange {

    private final Map<AttributeKey<?>, Object> attributes = new HashMap<>();
    private final AtomicBoolean released = new AtomicBoolean(false);
    private ChannelHandlerContext ctx;
    private FullHttpRequest fullHttpRequest;
    private boolean keepAlive;
    private boolean completed;
    private final AtomicBoolean written = new AtomicBoolean(false);
    private boolean occurError;
    private Throwable error;
    private Request request;

    private RequestMutable requestMutable;

    private RequestDubbo requestDubbo;

    private HttpHeaders responseHeaders = new DefaultHttpHeaders();

    private Response response;

    private RouteReadonly route;

    public DefaultExchange(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        this.ctx = ctx;
        this.keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
        this.fullHttpRequest = fullHttpRequest;
        this.request = new DefaultRequest(fullHttpRequest, (InetSocketAddress) ctx.channel().remoteAddress());
        this.route = matchRoute(this.request);
        if (Protocol.isHttp(getProtocol())) {
            this.requestMutable = new DefaultRequestMutable(request, fullHttpRequest);
        } else if (Protocol.isDubbo(getProtocol())) {
            this.requestDubbo = new RequestDubbo(request);
        } else {
            log.error("unknown protocol");
        }
    }

    @Override
    public <T> T getAttribute(AttributeKey<T> key) {
        return (T) attributes.get(key);
    }

    @Override
    public <T> T putAttribute(AttributeKey<T> key, T value) {
        return (T) attributes.put(key, value);
    }

    @Override
    public <T> T getRequiredAttribute(AttributeKey<T> key) {
        T value = getAttribute(key);
        Assert.notNull(value, () -> "Required attribute '" + key + "' is missing");
        return value;
    }

    @Override
    public <T> T getAttributeOrDefault(AttributeKey<T> key, T defaultValue) {
        return (T) attributes.getOrDefault(key, defaultValue);
    }

    @Override
    public void completedAndResponse(Response response) {
        if (response != null) {
            this.response = response;
            this.response.addHeader(responseHeaders);
            this.completed = true;
        }
    }

    @Override
    public void occurError(Throwable error) {
        if (error != null) {
            this.occurError = true;
            this.error = error;
        }
    }

    @Override
    public boolean isError() {
        return this.occurError;
    }

    @Override
    public Throwable getError() {
        return this.error;
    }

    @Override
    public boolean isCompleted() {
        return this.completed || this.occurError;
    }

    @Override
    public AtomicBoolean getWritten() {
        return written;
    }

    @Override
    public String getProtocol() {
        return this.route.getProtocol();
    }

    @Override
    public FilterConfig getFilterConfig(String name) {
        return this.route.getFilterConfig(name);
    }

    @Override
    public Request getRequest() {
        return this.request;
    }

    @Override
    public RequestMutable getRequestMutable() {
        return this.requestMutable;
    }

    @Override
    public Response getResponse() {
        return this.response;
    }

    @Override
    public void addResponseHeader(String name, String key) {
        if (this.response != null) {
            this.response.addHeader(name, key);
        } else {
            responseHeaders.add(name, key);
        }
    }

    @Override
    public void releaseRequest() {
        if (released.compareAndSet(false, true)) {
            ReferenceCountUtil.release(fullHttpRequest);
        }
    }

    @Override
    public RouteReadonly getRoute() {
        return this.route;
    }


    private RouteReadonly matchRoute(Request request) {
        RouteReadonly defaultRoute = matchRoutes(request, DefaultDynamicConfig.getInstance().getDefaultRoutes());
        if (defaultRoute == null) {
            if (!request.getPath().equals("/") && !request.getPath().equals("/favicon.ico")) {
                log.info("Searching for the default Route does not find the group. Original request：{}", request.getUrl());
            }
            throw new DamException(ErrorCode.NOT_FOUND);
        }
        SortedSet<Route> routes = DefaultDynamicConfig.getInstance().getRoutes(defaultRoute.getGroup());
        List<Route> noDefaultRoutes = routes.stream().filter(route -> !route.isGlobal()).collect(Collectors.toList());
        RouteReadonly route = matchRoutes(request, noDefaultRoutes);
        if (route == null) {
            route = matchRoutes(request, routes.stream().filter(Route::isGlobal).collect(Collectors.toList()));
            if (route == null) {
                log.info("Route information not found. group:{} Original request：{}", defaultRoute.getGroup(), request.getUrl());
                throw new DamException(ErrorCode.NOT_FOUND);
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

    @Override
    public ChannelHandlerContext getChannelHandlerContext() {
        return ctx;
    }

    @Override
    public boolean isKeepAlive() {
        return keepAlive;
    }
}
