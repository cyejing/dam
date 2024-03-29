package cn.cyejing.dam.core.context;

import cn.cyejing.dam.common.config.FilterConfig;
import cn.cyejing.dam.common.config.RouteReadonly;


public interface Exchange {

    <T> T getAttribute(AttributeKey<T> key);

    <T> T putAttribute(AttributeKey<T> key, T value);

    <T> T getRequiredAttribute(AttributeKey<T> key);

    <T> T getAttributeOrDefault(AttributeKey<T> key, T defaultValue);

    void completedAndResponse(Response response);

    void occurError(Throwable t);

    boolean isError();

    Throwable getError();

    boolean isCompleted();

    String getProtocol();

    FilterConfig getFilterConfig(String name);

    Request getRequest();

    RequestMutable getRequestMutable();

    Response getResponse();

    void addResponseHeader(String name, String key);

    void releaseRequest();

    RouteReadonly getRoute();

}
