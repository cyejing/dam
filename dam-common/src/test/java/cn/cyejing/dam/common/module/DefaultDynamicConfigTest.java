package cn.cyejing.dam.common.module;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultDynamicConfigTest {

    @Test
    public void testRouteUpdate() {
        DefaultDynamicConfig.getInstance().clear();
        Route route = new Route();
        route.setServiceName("foo");
        route.setId("1234");
        route.setName("foo1");
        route.setOrder(1);
        DefaultDynamicConfig.getInstance().addRoute(route);
        assertEquals("foo1",DefaultDynamicConfig.getInstance().getRoutes("foo").first().getName());

        Route route1 = new Route();
        route1.setServiceName("foo");
        route1.setId("1234");
        route1.setName("foo2");
        route1.setOrder(1);
        DefaultDynamicConfig.getInstance().addRoute(route1);
        assertEquals("foo2",DefaultDynamicConfig.getInstance().getRoutes("foo").first().getName());
    }

    @Test
    public void testRouteNoUpdate() {
        DefaultDynamicConfig.getInstance().clear();
        Route route = new Route();
        route.setServiceName("foo");
        route.setId("1234");
        route.setName("foo1");
        route.setOrder(1);
        DefaultDynamicConfig.getInstance().addRoute(route);
        assertEquals("foo1",DefaultDynamicConfig.getInstance().getRoutes("foo").first().getName());

        Route route1 = new Route();
        route1.setServiceName("foo");
        route1.setId("1235");
        route1.setName("foo2");
        route1.setOrder(1);
        DefaultDynamicConfig.getInstance().addRoute(route1);
        assertEquals(2, DefaultDynamicConfig.getInstance().getRoutes("foo").size());
        Route[] routes = DefaultDynamicConfig.getInstance().getRoutes("foo").toArray(new Route[]{});
        assertEquals("foo1", routes[0].getName());
        assertEquals("foo2", routes[1].getName());
    }



    @Test
    public void testAddDiffOrder() {
        DefaultDynamicConfig.getInstance().clear();
        Route route = new Route();
        route.setServiceName("foo");
        route.setId("1234");
        route.setName("foo1");
        route.setOrder(1);
        route.setDefaultRoute(true);
        DefaultDynamicConfig.getInstance().addRoute(route);
        assertEquals("foo1",DefaultDynamicConfig.getInstance().getRoutes("foo").first().getName());

        Route route1 = new Route();
        route1.setServiceName("foo");
        route1.setId("1234");
        route1.setName("foo2");
        route1.setOrder(1);
        route1.setDefaultRoute(true);
        DefaultDynamicConfig.getInstance().addRoute(route1);
        assertEquals(1, DefaultDynamicConfig.getInstance().getRoutes("foo").size());
        assertEquals(1, DefaultDynamicConfig.getInstance().getDefaultRoutes().size());
        assertEquals("foo2",DefaultDynamicConfig.getInstance().getRoutes("foo").first().getName());
        assertEquals("foo2",DefaultDynamicConfig.getInstance().getDefaultRoutes().stream().findFirst().get().getName());
    }

    @Test
    public void testAddInstance() {
        {
            DefaultDynamicConfig.getInstance().clear();
            Instance instance = new Instance();
            instance.setServiceName("123");
            instance.setAddress("123");
            instance.setRegisterTime(122L);
            DefaultDynamicConfig.getInstance().addInstance(instance);
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstancesNormal("123").toArray(new Instance[1]);
            assertEquals(1, ins.length);
            assertEquals("123", ins[0].getAddress());
        }

        {
            Instance instance = new Instance();
            instance.setServiceName("123");
            instance.setAddress("123");
            instance.setRegisterTime(123L);
            DefaultDynamicConfig.getInstance().addInstance(instance);
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstancesNormal("123").toArray(new Instance[1]);
            assertEquals(1, ins.length);
            assertEquals("123", ins[0].getAddress());
            assertEquals(123L, ins[0].getRegisterTime());
        }

        {
            Instance instance = new Instance();
            instance.setServiceName("123");
            instance.setAddress("124");
            instance.setRegisterTime(124L);
            DefaultDynamicConfig.getInstance().addInstance(instance);
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstancesNormal("123").toArray(new Instance[1]);
            assertEquals(2, ins.length);
            assertEquals("124", ins[0].getAddress());
            assertEquals(124L, ins[0].getRegisterTime());
        }

        {
            DefaultDynamicConfig.getInstance().deleteInstance("123", "124");
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstancesNormal("123").toArray(new Instance[1]);
            assertEquals(1, ins.length);
            assertEquals("123", ins[0].getAddress());
            assertEquals(123L, ins[0].getRegisterTime());
            DefaultDynamicConfig.getInstance().deleteInstance("123", "123");

            assertEquals(0, DefaultDynamicConfig.getInstance().getInstancesNormal("123").size());

        }

        {
            DefaultDynamicConfig.getInstance().clear();
            Instance instance = new Instance();
            instance.setServiceName("123");
            instance.setAddress("123");
            instance.setRegisterTime(122L);
            DefaultDynamicConfig.getInstance().addInstance(instance);
            Instance[] ins = DefaultDynamicConfig.getInstance().getInstancesNormal("123").toArray(new Instance[1]);
            assertEquals(1, ins.length);
            assertEquals("123", ins[0].getAddress());

            Instance[] ins1 = DefaultDynamicConfig.getInstance().getInstancesAll("123").toArray(new Instance[1]);
            assertEquals(1, ins1.length);
        }
    }

}
