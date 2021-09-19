package cn.cyejing.dam.registry.service;

import cn.cyejing.dam.common.module.Instance;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.registry.api.InstanceAPI;
import cn.cyejing.dam.registry.api.NotifyListener;
import cn.cyejing.dam.registry.spi.KeyValue;
import cn.cyejing.dam.registry.spi.RegistrySPI;
import cn.cyejing.dam.registry.spi.Watch;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


@Slf4j
public class InstanceServiceImpl extends AbstractService implements InstanceAPI {

    protected Queue<NotifyListener<Instance>> instanceListener = new ConcurrentLinkedQueue<>();
    protected Set<Instance> registeredInstances = new HashSet<>();

    public InstanceServiceImpl(RegistrySPI registrySPI) {
        super(registrySPI);
    }

    @Override
    public void registerInstance(Instance instance) {

    }

    @Override
    public void unregisterInstance(String host, String address) {

    }

    @Override
    public void subscribeInstance(NotifyListener<Instance> listener) {

    }

    @Override
    public Instance queryInstance(String host, String address) {
        return null;
    }

    @Override
    public List<Instance> queryInstanceList(String host) {
        return null;
    }

    public void watchInstance() {
        getRegistrySPI().addWatch(getKey(), new Watch() {
            @Override
            public void put(KeyValue keyValue) {
                log.info("watch instance updatekey: {}, value: {}", keyValue.getKey(), keyValue.getValue());
                for (NotifyListener<Instance> listener : instanceListener) {
                    try {
                        Instance instance = JSONUtil.parse(keyValue.getValue(), Instance.class);
                        listener.put(instance);
                    } catch (Exception e) {
                        log.error("resolve instance error", e);
                    }
                }
            }

            @Override
            public void delete(KeyValue keyValue) {
                log.info("watch instance delete key: {}, value: {}", keyValue.getKey(), keyValue.getValue());
                for (NotifyListener<Instance> listener : instanceListener) {
                    try {
                        Instance instance = JSONUtil.parse(keyValue.getValue(), Instance.class);
                        listener.delete(instance);
                    } catch (Exception e) {
                        log.error("resolve instance error", e);
                    }
                }
            }
        }, true);
    }

    private String getKey() {
        return "/instance";
    }

    private String getKey(String host, String address) {
        return getKey(host) + "/" + address;
    }

    private String getKey(String host) {
        return getKey() + "/" + host;
    }

}
