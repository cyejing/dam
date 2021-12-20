package cn.cyejing.dam.registry.memory;

import cn.cyejing.dam.common.config.DefaultDynamicConfig;
import cn.cyejing.dam.common.config.FilterConfig;
import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.spi.KeyValue;
import cn.cyejing.dam.registry.spi.NodePath;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * @author chenyejing
 */
public class FileConfigLoadTest {

    @Test
    public void testLoadFile() {
        MemoryRegistry memoryRegistry = new MemoryRegistry();
        new FileConfigLoad(memoryRegistry, new RegistryConfig("dam", "route-test.yaml")).load();
        KeyValue keyValue = memoryRegistry.get(NodePath.root("dam").genRouteKey("test1","196105db9a384d7a93f8102ae46684cb"), false).get(0);
        System.out.println(keyValue.getValue());
        Route route = JSONUtil.readValue(keyValue.getValue(),Route.class);
        assertNotNull(route);
        JsonNode config = JSONUtil.convertValue(route.getFilterConfig("rewrite").getParams(), JsonNode.class);
        assertEquals("/replace/(.*)", config.get("regex").asText());
        assertEquals("/$1", config.get("replacement").asText());

        JsonNode config2 = JSONUtil.convertValue(route.getFilterConfig("proxy").getParams(), JsonNode.class);
        assertEquals("rlb://test", config2.get("uri").asText());

        FilterConfig[] filterConfigs = route.getFilterConfigs().toArray(new FilterConfig[0]);
        assertEquals("rewrite",filterConfigs[0].getName());
    }

    @Test
    public void testLoadFile2() {
        MemoryRegistry memoryRegistry = new MemoryRegistry();
        new FileConfigLoad(memoryRegistry, new RegistryConfig("dam", "route-test.yaml")).load();
        KeyValue keyValue = memoryRegistry.get(NodePath.root("dam").genRouteKey("test2","126105db9a384d7a93f8102ae4668412"), false).get(0);
        Route route = JSONUtil.readValue(keyValue.getValue(),Route.class);
        assertNotNull(route);

        JsonNode config = JSONUtil.convertValue(route.getFilterConfig("rewrite").getParams(), JsonNode.class);
        assertEquals("/a/b/(?<segment>.*)", config.get("regex").asText());
        assertEquals("/f/${segment}",config.get("replacement").asText());

        JsonNode config1 = JSONUtil.convertValue(route.getFilterConfig("proxy").getParams(), JsonNode.class);
        assertEquals("http://localhost", config1.get("uri").asText());
    }

}
