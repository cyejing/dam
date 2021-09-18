
package cn.cyejing.dam.core.context;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RequestDubbo {

    private String registriesStr;
    private String interfaceClass;
    private String methodName;
    private String[] parameterTypes;
    private Object[] args;

    private int timeout;
    private String version;

    public RequestDubbo(Request request) {

    }
}
