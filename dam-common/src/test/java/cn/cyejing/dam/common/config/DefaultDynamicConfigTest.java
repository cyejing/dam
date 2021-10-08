package cn.cyejing.dam.common.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultDynamicConfigTest {

    @Test
    public void testRouteUpdate() {
        DefaultDynamicConfig.getInstance().clear();
        Route route = new Route();
        route.setGroup("foo");
        route.setId("1234");
        route.setOrder(1);
        DefaultDynamicConfig.getInstance().addRoute(route);
        assertEquals(route, DefaultDynamicConfig.getInstance().getRoutes("foo").first());

        Route route1 = new Route();
        route1.setGroup("foo");
        route1.setId("1234");
        route1.setOrder(2);
        DefaultDynamicConfig.getInstance().addRoute(route1);
        assertEquals(route1, DefaultDynamicConfig.getInstance().getRoutes("foo").first());
    }

    @Test
    public void testRouteNoUpdate() {
        DefaultDynamicConfig.getInstance().clear();
        Route route = new Route();
        route.setGroup("foo");
        route.setId("1234");
        route.setOrder(1);
        DefaultDynamicConfig.getInstance().addRoute(route);
        assertEquals(route, DefaultDynamicConfig.getInstance().getRoutes("foo").first());

        Route route1 = new Route();
        route1.setGroup("foo");
        route1.setId("1235");
        route1.setOrder(1);
        DefaultDynamicConfig.getInstance().addRoute(route1);
        assertEquals(2, DefaultDynamicConfig.getInstance().getRoutes("foo").size());
        Route[] routes = DefaultDynamicConfig.getInstance().getRoutes("foo").toArray(new Route[]{});
        assertEquals(route, routes[0]);
        assertEquals(route1, routes[1]);
    }


    @Test
    public void testAddDiffOrder() {
        DefaultDynamicConfig.getInstance().clear();
        Route route = new Route();
        route.setGroup("foo");
        route.setId("1234");
        route.setOrder(1);
        route.setGlobal(true);
        DefaultDynamicConfig.getInstance().addRoute(route);
        assertEquals(route, DefaultDynamicConfig.getInstance().getRoutes("foo").first());

        Route route1 = new Route();
        route1.setGroup("foo");
        route1.setId("1234");
        route1.setOrder(1);
        route1.setGlobal(true);
        DefaultDynamicConfig.getInstance().addRoute(route1);
        assertEquals(1, DefaultDynamicConfig.getInstance().getRoutes("foo").size());
        assertEquals(1, DefaultDynamicConfig.getInstance().getDefaultRoutes().size());
        assertEquals(route1, DefaultDynamicConfig.getInstance().getRoutes("foo").first());
        assertEquals(route1, DefaultDynamicConfig.getInstance().getDefaultRoutes().stream().findFirst().get());
    }

    @Test
    public void testAddInstance() {
        {
            DefaultDynamicConfig.getInstance().clear();
            Instance instance = new Instance();
            instance.setHost("123");
            instance.setAddress("123");
            instance.setRegisterTime(122L);
            DefaultDynamicConfig.getInstance().addInstance(instance);
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstances("123").toArray(new Instance[1]);
            assertEquals(1, ins.length);
            assertEquals("123", ins[0].getAddress());
        }

        {
            Instance instance = new Instance();
            instance.setHost("123");
            instance.setAddress("123");
            instance.setRegisterTime(123L);
            DefaultDynamicConfig.getInstance().addInstance(instance);
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstances("123").toArray(new Instance[1]);
            assertEquals(1, ins.length);
            assertEquals("123", ins[0].getAddress());
            assertEquals(123L, ins[0].getRegisterTime());
        }

        {
            Instance instance = new Instance();
            instance.setHost("123");
            instance.setAddress("124");
            instance.setRegisterTime(124L);
            DefaultDynamicConfig.getInstance().addInstance(instance);
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstances("123").toArray(new Instance[1]);
            assertEquals(2, ins.length);
            assertEquals("124", ins[0].getAddress());
            assertEquals(124L, ins[0].getRegisterTime());
        }

        {
            DefaultDynamicConfig.getInstance().deleteInstance("123", "124");
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstances("123").toArray(new Instance[1]);
            assertEquals(1, ins.length);
            assertEquals("123", ins[0].getAddress());
            assertEquals(123L, ins[0].getRegisterTime());
            DefaultDynamicConfig.getInstance().deleteInstance("123", "123");

            assertEquals(0, DefaultDynamicConfig.getInstance().getInstances("123").size());

        }

        {
            DefaultDynamicConfig.getInstance().clear();
            Instance instance = new Instance();
            instance.setHost("123");
            instance.setAddress("123");
            instance.setRegisterTime(122L);
            DefaultDynamicConfig.getInstance().addInstance(instance);
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstances("123").toArray(new Instance[1]);
            assertEquals(1, ins.length);
            assertEquals("123", ins[0].getAddress());

            Instance[] ins1 = DefaultDynamicConfig.getInstance().getInstances("123").toArray(new Instance[1]);
            assertEquals(1, ins1.length);
        }
    }

}
