package cn.cyejing.dam.registry.memory;

import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.RegistryServiceFactory;
import cn.cyejing.dam.registry.api.RouteAPI;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * @author chenyejing
 */
public class RouteServiceTest {

    @Test
    public void addRoute() {
        RegistryServiceFactory.getRegistryService().initialize(new RegistryConfig("test","route-test.yaml"));
        RouteAPI routeAPI = RegistryServiceFactory.getRegistryService().getRouteAPI();
        Route route = new Route();
        route.setGroup("test");
        route.setId("123");
        routeAPI.registerRoute(route);

        Assert.assertEquals(route,routeAPI.queryRoute(route.getGroup(), route.getId()));
    }

    @Test
    public void deleteRoute() {
        RegistryServiceFactory.getRegistryService().initialize(new RegistryConfig("test","route-test.yaml"));
        RouteAPI routeAPI = RegistryServiceFactory.getRegistryService().getRouteAPI();
        Route route = new Route();
        route.setGroup("test");
        route.setId("123");
        routeAPI.registerRoute(route);

        routeAPI.deleteRoute("test");

        Assert.assertFalse(CollectionUtils.isEmpty(routeAPI.queryRoute("test")));
    }
}
