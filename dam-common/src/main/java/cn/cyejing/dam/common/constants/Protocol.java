package cn.cyejing.dam.common.constants;


public abstract class Protocol {

    public static final String HTTP = "http";

    public static final String DUBBO = "dubbo";

    public static final String TCP = "tcp";

    public static boolean isHttp(String protocol) {
        return HTTP.equals(protocol);
    }

    public static boolean isDubbo(String protocol) {
        return DUBBO.equals(protocol);
    }

    public static boolean isTcp(String protocol) {
        return TCP.equals(protocol);
    }

    public static String getProtocol(String protocol) {
        if (isDubbo(protocol)) {
            return DUBBO;
        }
        if (isTcp(protocol)) {
            return TCP;
        }
        return HTTP;
    }

}
