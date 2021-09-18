package cn.cyejing.dam.registry;

import cn.cyejing.dam.registry.api.InstanceAPI;
import cn.cyejing.dam.registry.api.RouteAPI;


public interface RegistryService {

    void initialize(RegistryConfig config);

    void initialize(RegistryConfig config, boolean watch);

    InstanceAPI getInstanceAPI();

    RouteAPI getRouteAPI();
}
