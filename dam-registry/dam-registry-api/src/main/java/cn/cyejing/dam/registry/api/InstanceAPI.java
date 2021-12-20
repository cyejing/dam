package cn.cyejing.dam.registry.api;

import cn.cyejing.dam.common.config.Instance;

import java.util.List;


public interface InstanceAPI {

    void registerInstance(Instance instance);

    void unregisterInstance(String group, String uri);

    void subscribeInstance(NotifyListener<Instance> listener);

    Instance queryInstance(String host, String address);

    List<Instance> queryInstanceList(String host);

    List<Instance> queryInstanceList();

}
