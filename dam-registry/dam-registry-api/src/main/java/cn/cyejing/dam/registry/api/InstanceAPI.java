package cn.cyejing.dam.registry.api;

import cn.cyejing.dam.common.module.Instance;

import java.util.List;


public interface InstanceAPI {

    void registerInstance(Instance instance);

    void unregisterInstance(String serviceName, String address);

    void subscribeInstance(NotifyListener<Instance> listener);

    Instance queryInstance(String serviceName, String address);

    List<Instance> queryInstanceList(String serviceName);

}
