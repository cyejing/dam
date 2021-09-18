
package cn.cyejing.dam.common.module;

import java.util.Set;
import java.util.SortedSet;


public interface DynamicConfig {

    SortedSet<Route> getRoutes(String serviceName);

    void addRoute(Route route);

    void deleteRoute(String serviceName, String id);

    void addInstance(Instance instance);

    void deleteInstance(String serviceName, String address);

    Set<Instance> getInstancesNormal(String serviceName);

    Set<Instance> getInstancesAll(String serviceName);

    Set<Route> getDefaultRoutes();

    void clear();

}
