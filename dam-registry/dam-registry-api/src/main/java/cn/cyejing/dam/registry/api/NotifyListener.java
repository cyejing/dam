package cn.cyejing.dam.registry.api;


public interface NotifyListener<T> {

    void put(T t);

    void delete(T t);
}
