package cn.cyejing.dam.core.context;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Slf4j
public class DefaultRequest implements Request {
    private final FullHttpRequest fullHttpRequest;

    @Getter
    private final long beginTime;

    @Getter
    private final Charset charset;

    @Getter
    private final String clientIp;

    @Getter
    private final String host;

    @Getter
    private final String path;

    @Getter
    private final String uri;

    @Getter
    private final HttpMethod method;

    @Getter
    private final String contentType;

    @Getter
    private final HttpHeaders headers;

    @Getter
    private final QueryStringDecoder queryDecoder;


    private String body;

    private Map<String, Cookie> cookieMap;

    private Map<String, List<String>> postParameters;

    public DefaultRequest(FullHttpRequest request, InetSocketAddress remoteAddress) {
        this.fullHttpRequest = request;
        this.beginTime = System.currentTimeMillis();
        this.method = request.method();
        this.headers = request.headers();
        this.uri = request.uri();
        this.host = headers.get(HttpHeaderNames.HOST);
        this.clientIp = getClientIp(remoteAddress, request);
        this.contentType = HttpUtil.getMimeType(request) == null ? null : HttpUtil.getMimeType(request).toString();
        this.charset = HttpUtil.getCharset(request, StandardCharsets.UTF_8);

        this.queryDecoder = new QueryStringDecoder(uri, charset);
        this.path = queryDecoder.path();
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return queryDecoder.parameters();
    }

    @Override
    public List<String> getQueryParametersMultiple(String name) {
        return queryDecoder.parameters().get(name);
    }


    @Override
    public String getQueryParameters(String name) {
        if (getQueryParametersMultiple(name) == null) {
            return null;
        }
        return getQueryParametersMultiple(name).get(0);
    }

    @Override
    public String getQueryStr() {
        return queryDecoder.rawQuery();
    }


    @Override
    public Cookie getCookie(String name) {
        if (cookieMap == null) {
            cookieMap = new HashMap<>();
            String cookieStr = getHeaders().get(HttpHeaderNames.COOKIE);
            Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieStr);
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.name(), cookie);
            }
        }
        return cookieMap.get(name);
    }


    @Override
    public String getPostParameters(String name) {
        return getPostParametersMultiple(name).get(0);
    }

    @Override
    public List<String> getPostParametersMultiple(String name) {
        String body = getBody();
        if (isFormPost() && postParameters == null) {
            QueryStringDecoder paramDecoder = new QueryStringDecoder(body, false);
            postParameters = paramDecoder.parameters();
        }
        return postParameters == null || postParameters.get(name) == null ? Collections.emptyList() : postParameters.get(name);
    }

    @Override
    public String getBody() {
        if (StringUtils.isEmpty(body)) {
            body = fullHttpRequest.content().toString(charset);
        }
        return body;
    }

    @Override
    public String getUrl() {
        return "http://" + getHost() + getPath();
    }


    @Override
    public boolean isFormPost() {
        return HttpMethod.POST.equals(method) &&
                (contentType.startsWith(HttpHeaderValues.FORM_DATA.toString()) ||
                        contentType.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString()));
    }

    @Override
    public boolean isJsonPost() {
        return HttpMethod.POST.equals(method) && contentType.startsWith(HttpHeaderValues.APPLICATION_JSON.toString());
    }


    private String getClientIp(InetSocketAddress remoteAddress, FullHttpRequest request) {
        String xForwardedValue = request.headers().get("X-Forwarded-For");
        String clientIp = null;
        if (StringUtils.isNotEmpty(xForwardedValue)) {
            List<String> values = Arrays.asList(xForwardedValue.split(", "));
            if (values.size() >= 1 && StringUtils.isNotBlank(values.get(0))) {
                clientIp = values.get(0);
            }
        }
        if (clientIp == null) {
            clientIp = remoteAddress.getAddress().getHostAddress();
        }
        return clientIp;
    }
}

