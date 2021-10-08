package cn.cyejing.dam.common.config;

import java.util.Set;
import java.util.SortedSet;


public interface DynamicConfig {

    Route getRoute(String group, String id);

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
