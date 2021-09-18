package cn.cyejing.dam.core.filter;

import cn.cyejing.dam.core.context.Exchange;

public interface Filter<T> {

    String getName();

    int getOrder();

    boolean isGlobal();

    Class<T> getConfigClass();

    void filter(Exchange exchange, T config, FilterChain chain);

}
