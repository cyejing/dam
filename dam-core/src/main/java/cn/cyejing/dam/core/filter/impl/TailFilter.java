package cn.cyejing.dam.core.filter.impl;

import cn.cyejing.dam.core.context.DefaultResponse;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.exception.DamException;
import cn.cyejing.dam.core.exception.ErrorCode;
import cn.cyejing.dam.core.filter.Filter;
import cn.cyejing.dam.core.filter.FilterChain;

public class TailFilter implements Filter {
    @Override
    public String getName() {
        return "tail";
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

    @Override
    public Class getConfigClass() {
        return null;
    }

    @Override
    public void filter(Exchange exchange, Object config, FilterChain chain) {
        exchange.occurError(new DamException(ErrorCode.FILTER_TAIL));
    }
}
