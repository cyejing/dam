package cn.cyejing.dam.registry.service;

import cn.cyejing.dam.common.config.Instance;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.registry.api.InstanceAPI;
import cn.cyejing.dam.registry.api.NotifyListener;
import cn.cyejing.dam.registry.spi.KeyValue;
import cn.cyejing.dam.registry.spi.NodePath;
import cn.cyejing.dam.registry.spi.RegistrySPI;
import cn.cyejing.dam.registry.spi.Watch;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


@Slf4j
public class InstanceServiceImpl extends AbstractService implements InstanceAPI {

    protected Queue<NotifyListener<Instance>> instanceListener = new ConcurrentLinkedQueue<>();

    public InstanceServiceImpl(RegistrySPI registrySPI) {
        super(registrySPI);
    }

    @Override
    public void registerInstance(Instance instance) {
        getRegistrySPI().put(getNodePath().genInstanceKey(instance), JSONUtil.writeValueAsString(instance));
    }

    @Override
    public void unregisterInstance(String group, String uri) {
        getRegistrySPI().delete(getNodePath().genInstanceKey(group, uri), false);
    }

    @Override
    public void subscribeInstance(NotifyListener<Instance> listener) {
        instanceListener.add(listener);
        watchInstance();
        queryInstanceList().forEach(listener::put);
    }

    @Override
    public Instance queryInstance(String group, String uri) {
        return getRegistrySPI().get(getNodePath().genInstanceKey(group, uri), true).stream()
                .findFirst()
                .map(kv -> JSONUtil.readValue(kv.getValue(), Instance.class))
                .orElse(null);
    }

    @Override
    public List<Instance> queryInstanceList(String group) {
        return getRegistrySPI().get(getNodePath().genInstanceKey(group), true).stream()
                .map(kv -> JSONUtil.readValue(kv.getValue(), Instance.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Instance> queryInstanceList() {
        return getRegistrySPI().get(getNodePath().genInstanceKey(), true).stream()
                .map(kv -> JSONUtil.readValue(kv.getValue(), Instance.class))
                .collect(Collectors.toList());
    }

    public void watchInstance() {
        getRegistrySPI().addWatch(NodePath.root(getNamespace()).genInstanceKey(), new Watch() {
            @Override
            public void put(KeyValue keyValue) {
                log.info("watch instance updatekey: {}, value: {}", keyValue.getKey(), keyValue.getValue());
                for (NotifyListener<Instance> listener : instanceListener) {
                    try {
                        Instance instance = JSONUtil.readValue(keyValue.getValue(), Instance.class);
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
                        Instance instance = JSONUtil.readValue(keyValue.getValue(), Instance.class);
                        listener.delete(instance);
                    } catch (Exception e) {
                        log.error("resolve instance error", e);
                    }
                }
            }
        }, true);
    }
}
