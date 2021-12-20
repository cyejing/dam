package cn.cyejing.dam.registry.spi;

import cn.cyejing.dam.registry.RegistryConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;


public interface RegistrySPI {

    void init(RegistryConfig config);

    String getNamespace();

    void put(String key, String value);

    void putWithHeartbeat(String key, String value);

    void putWithTimeout(String key, String value, int timeout, TimeUnit unit);

    void delete(String key, boolean withPrefix);

    List<KeyValue> get(String key, boolean withPrefix);

    void addWatch(String key, Watch watch, boolean prefix);

}
