package cn.cyejing.dam.common.config;

import cn.cyejing.dam.common.expression.Expression;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Data
public class Route implements Comparable<Route>, RouteReadonly {

    protected String id;
    protected String group;
    protected Integer order;
    protected String protocol;
    protected String expressionStr;
    protected Expression expression;
    protected List<FilterConfig> filterConfigs = new ArrayList<>();

    protected boolean global;

    protected boolean loggable;

    public boolean addFilterConfig(FilterConfig filterConfig) {
        return filterConfigs.add(filterConfig);
    }

    public FilterConfig getFilterConfig(String name) {
        for (FilterConfig filterConfig : filterConfigs) {
            if (filterConfig.getName().equalsIgnoreCase(name)) {
                return filterConfig;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Route o) {
        int orderCompare = Integer.compare(getOrder(), o.getOrder());
        if (orderCompare == 0) {
            return getId().compareTo(o.getId());
        }
        return orderCompare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public RouteReadonly toReadonly() {
        return this;
    }

}
