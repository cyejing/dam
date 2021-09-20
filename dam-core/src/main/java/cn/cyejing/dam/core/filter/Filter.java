package cn.cyejing.dam.core.filter;

import cn.cyejing.dam.core.context.Exchange;

public interface Filter<T> {

    String getName();

    default String getNameToLowerCase() {
        return getName().toLowerCase();
    }

    int getOrder();

    boolean isGlobal();

    Class<T> getConfigClass();

    void filter(FilterChain chain, Exchange exchange, T config) throws Exception;

}
