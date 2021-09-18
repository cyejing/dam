
package cn.cyejing.dam.common.module;

import cn.cyejing.dam.common.expression.Expression;

import java.util.Set;


public interface RouteReadonly {
    String getId();

    String getName();

    String getServiceName();

    Integer getOrder();

    String getExpressionStr();

    Expression getExpression();

    Set<FilterConfig> getFilterConfigs();

    boolean isLoggable();

    boolean isDefault();

    String getProtocol();

    FilterConfig getFilterConfig(String name);

    boolean hasName(String name);
}
