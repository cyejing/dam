package cn.cyejing.dam.registry.etcd;

import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.spi.KeyValue;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;


public class EtcdRegistryTest {

    @Test
    @Ignore
    public void testInit() {
        EtcdRegistry etcdRegistry = new EtcdRegistry();
        etcdRegistry.init(new RegistryConfig("/test", "http://localhost:2379"));

        etcdRegistry.put("/halo", "hello");

        List<KeyValue> keyValues = etcdRegistry.get("/halo", true);
        Assert.assertEquals("hello", keyValues.get(0).getValue());
    }


}
