package cn.cyejing.dam.core.config;

import cn.cyejing.dam.common.module.DefaultDynamicConfig;
import cn.cyejing.dam.common.module.Instance;
import cn.cyejing.dam.common.module.Route;
import cn.cyejing.dam.core.support.cache.CacheManager;
import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.RegistryService;
import cn.cyejing.dam.registry.RegistryServiceFactory;
import cn.cyejing.dam.registry.api.NotifyListener;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class DynamicConfigInitializer {
    private static final DynamicConfigInitializer INSTANCE = new DynamicConfigInitializer();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public static DynamicConfigInitializer getInstance() {
        return INSTANCE;
    }

    public boolean loadFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("rote file is not exist " + path);

        }
        final String absolutePath = file.getAbsolutePath();
        try {
            Yaml yaml = new Yaml();
            RouteConfig config = yaml.loadAs(new FileInputStream(absolutePath), RouteConfig.class);
            for (Route route : config.getRoutes()) {
                DefaultDynamicConfig.getInstance().addRoute(route);
                CacheManager.removeForRoute(route.getId());
            }
            for (Instance instance : config.getInstances()) {
                DefaultDynamicConfig.getInstance().addInstance(instance);
            }
            log.info(" reload route yaml success.");
            return true;
        } catch (Throwable t) {
            log.error("reload route yaml failed. path:{}", absolutePath, t);
            return false;
        }
    }

    public void watchRegistry(String namespace, String addresses) {
        if (initialized.compareAndSet(false, true)) {
            RegistryService registryService = RegistryServiceFactory.getRegistryService();
            registryService.initialize(new RegistryConfig(namespace, addresses), true);

            registryService.getRouteAPI().subscribeRoute(new NotifyListener<Route>() {
                @Override
                public void put(Route route) {
                    DefaultDynamicConfig.getInstance().addRoute(route);
                    CacheManager.removeForRoute(route.getId());
                }

                @Override
                public void delete(Route route) {
                    DefaultDynamicConfig.getInstance().deleteRoute(route.getGroup(), route.getId());
                }
            });

            registryService.getInstanceAPI().subscribeInstance(new NotifyListener<Instance>() {
                @Override
                public void put(Instance instance) {
                    DefaultDynamicConfig.getInstance().addInstance(instance);
                }

                @Override
                public void delete(Instance instance) {
                    DefaultDynamicConfig.getInstance().deleteInstance(instance.getHost(), instance.getAddress());
                }
            });
        }
    }
}
