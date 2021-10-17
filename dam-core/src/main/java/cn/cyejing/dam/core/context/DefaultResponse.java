package cn.cyejing.dam.core.context;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.http.*;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;


public class DefaultResponse implements Response {

    private final HttpResponse response;
    public FileRegion fileRegion;

    public DefaultResponse(String body) {
        this(HttpResponseStatus.OK, body, null);
    }

    public DefaultResponse(File file) {
        this.response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        this.fileRegion = new DefaultFileRegion(file, 0, file.length());
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, new MimetypesFileTypeMap().getContentType(file.getPath()));
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
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, body);
        if (headers != null) {
            response.headers().add(headers);
        }
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        this.response = response;
    }

    public DefaultResponse(org.asynchttpclient.Response response) {
        this.response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.valueOf(response.getStatusCode()),
                Unpooled.wrappedBuffer(response.getResponseBodyAsByteBuffer()));
        this.response.headers().add(response.getHeaders());
    }

    @Override
    public HttpResponseStatus getStatus() {
        return response.status();
    }

    @Override
    public void setStatus(HttpResponseStatus status) {
        response.setStatus(status);
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

    public HttpResponse build() {
        return response;
    }

    public FileRegion getFileRegion() {
        return fileRegion;
    }

}
