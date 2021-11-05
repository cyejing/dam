package cn.cyejing.dam.core.config;

import cn.cyejing.dam.common.config.DefaultDynamicConfig;
import cn.cyejing.dam.common.config.FilterConfig;
import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.filter.impl.ProxyFilter;
import cn.cyejing.dam.core.filter.impl.RewriteFilter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DynamicConfigInitializerTest {

    @Test
    public void testLoadFile() {
        DynamicConfigInitializer.getInstance().loadFile(DynamicConfigInitializer.class.getClassLoader().getResourceAsStream("route-test.yaml"));
        Route route = DefaultDynamicConfig.getInstance().getRoute("test1", "196105db9a384d7a93f8102ae46684cb");
        assertNotNull(route);
        FilterConfig filterConfig = route.getFilterConfig("rewrite");
        Object param = filterConfig.getParam();
        RewriteFilter.Config config = JSONUtil.convertValue(param, RewriteFilter.Config.class);
        assertEquals("/replace/(.*)", config.getRegex());
        assertEquals("/$1", config.getReplacement());

        FilterConfig filterConfig2 = route.getFilterConfig("proxy");
        ProxyFilter.Config config1 = JSONUtil.convertValue(filterConfig2.getParam(), ProxyFilter.Config.class);
        assertEquals("rlb://test", config1.getUri());
    }

    @Test
    public void testLoadFile2() {
        DynamicConfigInitializer.getInstance().loadFile(DynamicConfigInitializer.class.getClassLoader().getResourceAsStream("route-test.yaml"));
        Route route = DefaultDynamicConfig.getInstance().getRoute("test2", "126105db9a384d7a93f8102ae4668412");
        assertNotNull(route);
        FilterConfig filterConfig = route.getFilterConfig("rewrite");
        RewriteFilter.Config config = JSONUtil.convertValue(filterConfig.getParam(), RewriteFilter.Config.class);
        assertEquals("/a/b/(?<segment>.*)", config.getRegex());
        assertEquals("/f/${segment}", config.getReplacement());

        FilterConfig filterConfig1 = route.getFilterConfig("proxy");
        ProxyFilter.Config config1 = JSONUtil.convertValue(filterConfig1.getParam(), ProxyFilter.Config.class);
        assertEquals("http://localhost", config1.getUri());

    }

}
