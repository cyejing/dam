package cn.cyejing.dam.common.constants;


public abstract class Protocol {

    public static final String HTTP = "http";

    public static final String DUBBO = "dubbo";

    public static boolean isHttp(String protocol) {
        return HTTP.equals(protocol);
    }

    public static boolean isDubbo(String protocol) {
        return DUBBO.equals(protocol);
    }

}
