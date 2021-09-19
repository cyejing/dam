package cn.cyejing.dam.core.context;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;
import org.asynchttpclient.RequestBuilder;

import java.util.Objects;

public class DefaultRequestMutable implements RequestMutable {

    private RequestBuilder requestBuilder;
    private String scheme;
    private String host;
    private String path;

    public DefaultRequestMutable(Request request, FullHttpRequest fullHttpRequest) {
        this.scheme = "http://";
        this.host = request.getHost();
        this.path = request.getPath();

        this.requestBuilder = new RequestBuilder();
        this.requestBuilder.setMethod(request.getMethod().name());
        this.requestBuilder.setHeaders(request.getHeaders());
        this.requestBuilder.setQueryParams(request.getQueryParameters());
        if (Objects.nonNull(fullHttpRequest.content())) {
            this.requestBuilder.setBody(fullHttpRequest.content().nioBuffer());
        }
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getAddress() {
        return this.host = host;
    }

    @Override
    public void setAddress(String host) {
        this.host = host;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getRouteUrl() {
        return scheme + host + path;
    }

    @Override
    public void addHeader(CharSequence name, String value) {
        requestBuilder.addHeader(name, value);
    }

    @Override
    public void setHeader(CharSequence name, String value) {
        requestBuilder.setHeader(name, value);
    }

    @Override
    public void addQueryParam(String name, String value) {
        requestBuilder.addQueryParam(name, value);
    }

    @Override
    public void addOrReplaceCookie(Cookie cookie) {
        requestBuilder.addOrReplaceCookie(cookie);
    }

    @Override
    public void addFormParam(String name, String value) {
        requestBuilder.addFormParam(name, value);
    }

    @Override
    public void setRequestTimeout(int requestTimeout) {
        requestBuilder.setRequestTimeout(requestTimeout);

    }

    @Override
    public org.asynchttpclient.Request build() {
        requestBuilder.setUrl(getRouteUrl());
        return requestBuilder.build();
    }

}
