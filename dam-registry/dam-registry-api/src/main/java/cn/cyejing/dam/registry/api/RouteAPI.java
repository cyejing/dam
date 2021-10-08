package cn.cyejing.dam.registry.api;

import cn.cyejing.dam.common.config.Route;

import java.util.List;


public interface RouteAPI {

    void registerRoute(Route route);

    void subscribeRoute(NotifyListener<Route> listener);

    void deleteRoute(String group, String routeId);

    void deleteRoute(String group);

    Route queryRoute(String group, String routeId);

    List<Route> queryRoutes();

    List<Route> queryRoute(String group);

}
