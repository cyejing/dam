
package cn.cyejing.dam.core.context;

import io.netty.handler.codec.http.cookie.Cookie;
import org.asynchttpclient.Request;


public interface RequestMutable {

    
    String getScheme();
    void setScheme(String scheme);


    String getHost();

    void setHost(String host);

    String getPath();

    void setPath(String path);

    String getRouteUrl();

    void addHeader(CharSequence name, String value);

    
    void setHeader(CharSequence name, String value);

    
    void addQueryParam(String name, String value);

    
    void addOrReplaceCookie(Cookie cookie);

    
    void addFormParam(String name, String value);

    
    void setRequestTimeout(int requestTimeout);

    Request build();


}
