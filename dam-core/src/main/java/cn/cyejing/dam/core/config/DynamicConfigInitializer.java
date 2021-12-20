package cn.cyejing.dam.core.config;

import cn.cyejing.dam.common.config.DefaultDynamicConfig;
import cn.cyejing.dam.common.config.Instance;
import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.core.support.cache.CacheManager;
import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.RegistryService;
import cn.cyejing.dam.registry.RegistryServiceFactory;
import cn.cyejing.dam.registry.api.NotifyListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class DynamicConfigInitializer {
    private static final DynamicConfigInitializer INSTANCE = new DynamicConfigInitializer();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public static DynamicConfigInitializer getInstance() {
        return INSTANCE;
    }

    public void watchRegistry(String namespace, String urls) {
        if (initialized.compareAndSet(false, true)) {
            RegistryService registryService = RegistryServiceFactory.getRegistryService();
            registryService.initialize(new RegistryConfig(namespace, urls));

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
                    DefaultDynamicConfig.getInstance().deleteInstance(instance.getGroup(), instance.getUri());
                }
            });
        }
    }
}
