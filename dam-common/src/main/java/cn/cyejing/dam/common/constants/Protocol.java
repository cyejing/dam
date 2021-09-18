package cn.cyejing.dam.common.constants;


public interface Protocol {

	String HTTP = "http";

	String DUBBO = "dubbo";

	String TCP = "tcp";

	static boolean isHttp(String protocol) {
	    return HTTP.equals(protocol);
	}

	static boolean isDubbo(String protocol) {
        return DUBBO.equals(protocol);
    }

	static boolean isTcp(String protocol) {
		return TCP.equals(protocol);
	}

    static String getProtocol(String protocol)
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
