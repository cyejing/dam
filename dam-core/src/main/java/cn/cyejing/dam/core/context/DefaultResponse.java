package cn.cyejing.dam.core.context;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;


public class DefaultResponse implements Response {

    private final FullHttpResponse response;

    public DefaultResponse(String body) {
        this(HttpResponseStatus.OK, body, null);
    }

    public DefaultResponse(ByteBuf body) {
        this(HttpResponseStatus.OK, body, null);
    }

    public DefaultResponse(String body, HttpHeaders headers) {
        this(HttpResponseStatus.OK, body, headers);
    }

    public DefaultResponse(ByteBuf body, HttpHeaders headers) {
        this(HttpResponseStatus.OK, body, headers);
    }

    public DefaultResponse(HttpResponseStatus status, String body) {
        this(status, Unpooled.wrappedBuffer(body.getBytes()), null);
    }

    public DefaultResponse(HttpResponseStatus status, String body, HttpHeaders headers) {
        this(status, Unpooled.wrappedBuffer(body.getBytes()), headers);
    }

    public DefaultResponse(HttpResponseStatus status, ByteBuf body, HttpHeaders headers) {
        this.response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, body);
        if (headers != null) {
            response.headers().add(headers);
        }
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    }


    @Override
    public void setStatus(HttpResponseStatus status) {
        response.setStatus(status);
    }

    @Override
    public HttpResponseStatus getStatus() {
        return response.status();
    }

    @Override
    public void addHeader(CharSequence key, CharSequence val) {
        response.headers().add(key, val);
    }

    @Override
    public void addHeader(HttpHeaders httpHeaders) {
        response.headers().add(httpHeaders);
    }

    @Override
    public void setHeader(CharSequence key, CharSequence val) {
        response.headers().set(key, val);
    }

    @Override
    public void setHeader(HttpHeaders httpHeaders) {
        response.headers().set(httpHeaders);
    }


    @Override
    public ByteBuf getBody() {
        return response.content();
    }

    public FullHttpResponse build() {
        return response;
    }

}
