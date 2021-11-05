package cn.cyejing.dam.core.context;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.cookie.Cookie;
import org.asynchttpclient.Request;


public interface RequestMutable {

    String getPath();

    void setPath(String path);

    String getUri();

    void setUri(String uri);

    void addHeader(CharSequence name, String value);

    void setHeader(CharSequence name, String value);

    void setHeaders(HttpHeaders headers);

    void unsetHeader(CharSequence name);

    void clearHeaders();

    void addQueryParam(String name, String value);

    void addOrReplaceCookie(Cookie cookie);

    void addFormParam(String name, String value);

    void setRequestTimeout(int requestTimeout);

    Request build();


}
