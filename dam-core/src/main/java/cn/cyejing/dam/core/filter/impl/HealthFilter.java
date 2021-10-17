package cn.cyejing.dam.core.filter.impl;

import cn.cyejing.dam.common.config.DefaultDynamicConfig;
import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.common.constants.Protocol;
import cn.cyejing.dam.core.context.DefaultResponse;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.filter.Filter;
import cn.cyejing.dam.core.filter.FilterChain;
import cn.cyejing.dam.core.filter.GlobalFilter;

public class HealthFilter implements GlobalFilter {

    public HealthFilter() {
        Route route = new Route();
        route.setId("health");
        route.setExpressionStr("Path.Equals('/health')");
        route.setGlobal(true);
        route.setProtocol(Protocol.HTTP);
        route.setOrder(0);
        route.setGroup("health");
        DefaultDynamicConfig.getInstance().addRoute(route);
    }

    @Override
    public String getName() {
        return "health";
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public Class getConfigClass() {
        return null;
    }

    @Override
    public void filter(FilterChain chain, Exchange exchange, Object config) throws Exception {
        if (exchange.getRequest().getPath().equals("/health")) {
            exchange.completedAndResponse(new DefaultResponse("up"));
        }
        chain.doFilter();
    }
}
