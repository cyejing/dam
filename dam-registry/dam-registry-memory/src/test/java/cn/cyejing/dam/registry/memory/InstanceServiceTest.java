package cn.cyejing.dam.registry.memory;

import cn.cyejing.dam.common.config.Instance;
import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.RegistryServiceFactory;
import cn.cyejing.dam.registry.api.InstanceAPI;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chenyejing
 */
public class InstanceServiceTest {

    @Test
    public void addInstance() {
        RegistryServiceFactory.getRegistryService().initialize(new RegistryConfig("test","route-test.yaml"));
        InstanceAPI instanceAPI = RegistryServiceFactory.getRegistryService().getInstanceAPI();
        Instance instance = new Instance();
        instance.setGroup("test");
        instance.setUri("123");
        instanceAPI.registerInstance(instance);

        Assert.assertEquals(instance, instanceAPI.queryInstance("test", "123"));
    }

    @Test
    public void deleteInstance() {
        RegistryServiceFactory.getRegistryService().initialize(new RegistryConfig("test","route-test.yaml"));
        InstanceAPI instanceAPI = RegistryServiceFactory.getRegistryService().getInstanceAPI();
        Instance instance = new Instance();
        instance.setGroup("test");
        instance.setUri("123");
        instanceAPI.registerInstance(instance);

        instanceAPI.unregisterInstance("test","123");

        Assert.assertFalse(CollectionUtils.isEmpty(instanceAPI.queryInstanceList("test")));
    }
}
