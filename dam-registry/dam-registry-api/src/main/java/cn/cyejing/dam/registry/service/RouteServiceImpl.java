package cn.cyejing.dam.registry.service;

import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.registry.api.NotifyListener;
import cn.cyejing.dam.registry.api.RouteAPI;
import cn.cyejing.dam.registry.spi.KeyValue;
import cn.cyejing.dam.registry.spi.RegistrySPI;
import cn.cyejing.dam.registry.spi.Watch;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Slf4j
public class RouteServiceImpl extends AbstractService implements RouteAPI {

    protected Queue<NotifyListener<Route>> routeListener = new ConcurrentLinkedQueue<>();

    public RouteServiceImpl(RegistrySPI registrySPI) {
        super(registrySPI);
    }

    @Override
    public void registerRoute(Route route) {

    }

    @Override
    public void subscribeRoute(NotifyListener<Route> listener) {

    }

    @Override
    public void deleteRoute(String group, String routeId) {

    }

    @Override
    public void deleteRoute(String group) {

    }

    @Override
    public Route queryRoute(String group, String routeId) {
        return null;
    }

    @Override
    public List<Route> queryRoutes() {
        return null;
    }

    @Override
    public List<Route> queryRoute(String group) {
        return null;
    }


    public void watchRoute() {
        getRegistrySPI().addWatch(getKey(), new Watch() {
            @Override
            public void put(KeyValue keyValue) {
                log.info("Listening to node changes key: {}, value: {}", keyValue.getKey(), keyValue.getValue());
                for (NotifyListener<Route> listener : routeListener) {
                    try {
                        Route route = JSONUtil.readValue(keyValue.getValue(), Route.class);
                        listener.put(route);
                    } catch (Exception e) {
                        log.error("Handling node changes with exceptions", e);
                    }
                }
            }

            @Override
            public void delete(KeyValue keyValue) {
                log.info("Listening to node deletion key: {}", keyValue.getKey());
                for (NotifyListener<Route> listener : routeListener) {
                    try {
                        Route route = JSONUtil.readValue(keyValue.getValue(), Route.class);
                        listener.delete(route);
                    } catch (Exception e) {
                        log.error("Handling node changes with exceptions", e);
                    }
                }
            }
        }, true);
    }


    private String getKey() {
        return "/routes";
    }

    private String getKey(String group, String id) {
        return getKey(group) + "/" + id;
    }

    private String getKey(String group) {
        return getKey() + "/" + group;
    }
}
