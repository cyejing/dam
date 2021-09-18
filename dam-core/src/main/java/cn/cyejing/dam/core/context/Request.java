
package cn.cyejing.dam.core.context;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cookie.Cookie;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;


public interface Request {

    long getBeginTime();

    Charset getCharset();

    String getClientIp();

    String getHost();

    String getUri();

    String getPath();

    HttpMethod getMethod();

    String getContentType();

    HttpHeaders getHeaders();

    Map<String, List<String>> getQueryParameters();

    List<String> getQueryParametersMultiple(String name);

    String getQueryParameters(String name);

    String getQueryStr();


    Cookie getCookie(String name);

    String getPostParameters(String name);

    List<String> getPostParametersMultiple(String name);

    boolean isFormPost();

    boolean isJsonPost();

    String getBody();

    String getUrl();
}
