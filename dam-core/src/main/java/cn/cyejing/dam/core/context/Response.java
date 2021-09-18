package cn.cyejing.dam.core.context;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

public interface Response {

    void setStatus(HttpResponseStatus status);

    HttpResponseStatus getStatus();

    void addHeader(CharSequence key, CharSequence val);

    void addHeader(HttpHeaders httpHeaders);

    void setHeader(CharSequence key, CharSequence val);

    void setHeader(HttpHeaders httpHeaders);

    ByteBuf getBody();

    FullHttpResponse build();

}
