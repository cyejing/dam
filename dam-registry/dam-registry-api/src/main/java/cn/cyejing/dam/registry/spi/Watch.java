package cn.cyejing.dam.registry.spi;


public interface Watch {

    void put(KeyValue keyValue);

    void delete(KeyValue keyValue);
}
