package cn.cyejing.dam.core.context;

import io.netty.channel.FileRegion;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public interface Response {

    HttpResponseStatus getStatus();

    void setStatus(HttpResponseStatus status);

    void addHeader(CharSequence key, CharSequence val);

    void addHeader(HttpHeaders httpHeaders);

    void setHeader(CharSequence key, CharSequence val);

    void setHeader(HttpHeaders httpHeaders);

    HttpResponse build();

    FileRegion getFileRegion();

}
