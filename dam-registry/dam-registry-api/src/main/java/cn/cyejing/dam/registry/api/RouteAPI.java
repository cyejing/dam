package cn.cyejing.dam.registry.api;

import cn.cyejing.dam.common.module.Route;

import java.util.List;


public interface RouteAPI {

    void registerRoute(Route route);

    void subscribeRoute(NotifyListener<Route> listener);

    void deleteRoute(String serviceName, String routeId);

    void deleteRoute(String serviceName);

    Route queryRoute(String serviceName, String routeId);

    List<Route> queryRoutes();

    List<Route> queryRoute(String serviceName);

}
