package cn.cyejing.dam.common.module;

import java.util.Set;
import java.util.SortedSet;


public interface DynamicConfig {

    SortedSet<Route> getRoutes(String group);

    void addRoute(Route route);

    void deleteRoute(String group, String id);

    void addInstance(Instance instance);

    void deleteInstance(String host, String address);

    Set<Instance> getInstances(String host);

    Set<Instance> getInstances(String host,String tag) ;

    Set<Route> getDefaultRoutes();

    void clear();

}
