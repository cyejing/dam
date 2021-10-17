package cn.cyejing.dam.core.filter;

public interface GlobalFilter<T> extends Filter<T>{
    int getOrder();
}
