package cn.cyejing.dam.core.context;

import cn.cyejing.dam.common.config.FilterConfig;
import cn.cyejing.dam.common.config.RouteReadonly;
import cn.cyejing.dam.common.constants.Protocol;
import cn.cyejing.dam.common.utils.Assert;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public DefaultExchange(ChannelHandlerContext ctx,
                           FullHttpRequest fullHttpRequest,
                           Request request,
                           RouteReadonly route) {
        this.ctx = ctx;
        this.keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
        this.fullHttpRequest = fullHttpRequest;
        this.request = request;
        this.route = route;
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


    @Override
    public ChannelHandlerContext getChannelHandlerContext() {
        return ctx;
    }

    @Override
    public boolean isKeepAlive() {
        return keepAlive;
    }
}
