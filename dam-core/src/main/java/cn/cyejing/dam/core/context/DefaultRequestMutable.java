package cn.cyejing.dam.core.context;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.cookie.Cookie;
import org.asynchttpclient.RequestBuilder;

import java.util.Objects;

public class DefaultRequestMutable implements RequestMutable {

    private RequestBuilder requestBuilder;
    private String uri;
    private String path;

    public DefaultRequestMutable(Request request, FullHttpRequest fullHttpRequest) {
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
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getUri() {
        return this.uri + path;
    }

    @Override
    public void  setUri(String uri) {
        this.uri = uri;
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
    public void setHeaders(HttpHeaders headers) {
        requestBuilder.setHeaders(headers);
    }

    @Override
    public void clearHeaders() {
        requestBuilder.clearHeaders();
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
        requestBuilder.setUrl(getUri());
        return requestBuilder.build();
    }

}
