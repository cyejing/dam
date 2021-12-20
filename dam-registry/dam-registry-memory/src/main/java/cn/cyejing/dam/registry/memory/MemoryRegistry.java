package cn.cyejing.dam.registry.memory;

import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.spi.KeyValue;
import cn.cyejing.dam.registry.spi.RegistrySPI;
import cn.cyejing.dam.registry.spi.Watch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenyejing
 */
public class MemoryRegistry implements RegistrySPI {

    final static ConcurrentHashMap<String, String> ROUTE_CONFIG = new ConcurrentHashMap<>();

    private final List<WatchNode> watches = new ArrayList<>();
    private RegistryConfig registryConfig;

    @Override
    public void init(RegistryConfig config) {
        this.registryConfig = config;
        new FileConfigLoad(this, config).load();
    }

    @Override
    public String getNamespace() {
        return registryConfig.getNamespace();
    }

    @Override
    public void put(String key, String value) {
        ROUTE_CONFIG.put(key, value);
        watches.stream()
                .filter(w -> (w.isPrefix() && w.getKey().startsWith(key)) || (!w.isPrefix() && w.getKey().equals(key)))
                .forEach(w -> w.getWatch().put(new KeyValue(key, value)));
    }

    @Override
    public void putWithHeartbeat(String key, String value) {
        ROUTE_CONFIG.put(key, value);
    }

    @Override
    public void putWithTimeout(String key, String value, int timeout, TimeUnit unit) {
        ROUTE_CONFIG.put(key, value);
    }

    @Override
    public void delete(String key, boolean withPrefix) {
        String value = ROUTE_CONFIG.remove(key);
        watches.stream()
                .filter(w -> (w.isPrefix() && w.getKey().startsWith(key)) || (!w.isPrefix() && w.getKey().equals(key)))
                .forEach(w -> w.getWatch().delete(new KeyValue(key, value)));
    }

    @Override
    public List<KeyValue> get(String key, boolean withPrefix) {
        return ROUTE_CONFIG.entrySet().stream()
                .filter(e -> e.getKey().startsWith(key))
                .map(e -> new KeyValue(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void addWatch(String key, Watch watch, boolean prefix) {
        watches.add(new WatchNode(key, watch, prefix));
    }
}
