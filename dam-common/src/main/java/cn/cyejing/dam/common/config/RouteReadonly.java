package cn.cyejing.dam.common.config;

import cn.cyejing.dam.common.expression.Expression;

import java.util.Set;


public interface RouteReadonly {
    String getId();
    String getGroup();

    boolean isGlobal();

    Integer getOrder();

    String getExpressionStr();

    Expression getExpression();

    Set<FilterConfig> getFilterConfigs();

    boolean isLoggable();

    String getProtocol();

    FilterConfig getFilterConfig(String name);

}
