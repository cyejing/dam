package cn.cyejing.dam.core.filter.impl;

import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.exception.DamException;
import cn.cyejing.dam.core.exception.ErrorCode;
import cn.cyejing.dam.core.filter.FilterChain;
import cn.cyejing.dam.core.filter.GlobalFilter;

public class TailFilter implements GlobalFilter {
    @Override
    public String getName() {
        return "tail";
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Class getConfigClass() {
        return null;
    }

    @Override
    public void filter(FilterChain chain, Exchange exchange, Object config) {
        exchange.occurError(new DamException(ErrorCode.FILTER_TAIL));
        chain.doFilter();
    }

}
