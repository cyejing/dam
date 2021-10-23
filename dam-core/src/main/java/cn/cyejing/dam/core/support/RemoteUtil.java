package cn.cyejing.dam.core.support;

import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class RemoteUtil {

    public static String getClientIp(InetSocketAddress remoteAddress, FullHttpRequest request) {
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
