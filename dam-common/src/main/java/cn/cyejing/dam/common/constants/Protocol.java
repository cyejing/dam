package cn.cyejing.dam.common.constants;


public abstract class Protocol {

	private static String HTTP = "http";

	private static String DUBBO = "dubbo";

	private static String TCP = "tcp";

	public static boolean isHttp(String protocol) {
	    return HTTP.equals(protocol);
	}

	public static boolean isDubbo(String protocol) {
        return DUBBO.equals(protocol);
    }

	public static boolean isTcp(String protocol) {
		return TCP.equals(protocol);
	}

	public static String getProtocol(String protocol)
	{
		if (isDubbo(protocol)) {
			return DUBBO;
		}
		if (isTcp(protocol)) {
			return TCP;
		}
		return HTTP;
	}

}
