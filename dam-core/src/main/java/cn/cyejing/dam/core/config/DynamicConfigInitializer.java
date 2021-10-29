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
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class DynamicConfigInitializer {
    private static final DynamicConfigInitializer INSTANCE = new DynamicConfigInitializer();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public static DynamicConfigInitializer getInstance() {
        return INSTANCE;
    }

    public boolean loadFile(String path) {
        InputStream inputStream = null;
        try {
            if (new File(path).exists()) {
                inputStream = new FileInputStream(path);
            } else {
                String relativePath = System.getenv("OLDPWD") + "/" + path;
                if (new File(relativePath).exists()) {
                    inputStream = new FileInputStream(relativePath);
                }
            }
        } catch (FileNotFoundException e) {
        }
        if (inputStream == null) {
            inputStream = DynamicConfigInitializer.class.getClassLoader().getResourceAsStream(path);
            log.info("load route yaml classpath: {}", path);
        } else {
            log.info("load route yaml path: {}", path);
        }
        return loadFile(inputStream);
    }

    public boolean loadFile(InputStream inputStream) {
        if (inputStream == null) {
            throw new RuntimeException("rote yaml is not exist.");
        }

        try {
            Yaml yaml = new Yaml();
            RouteConfig config = yaml.loadAs(inputStream, RouteConfig.class);
            for (Route route : config.getRoutes()) {
                DefaultDynamicConfig.getInstance().addRoute(route);
                CacheManager.removeForRoute(route.getId());
            }
            for (Instance instance : config.getInstances()) {
                DefaultDynamicConfig.getInstance().addInstance(instance);
            }
            log.info("reload route yaml success.");
            return true;
        } catch (Throwable t) {
            log.error("reload route yaml failed", t);
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
                    DefaultDynamicConfig.getInstance().deleteInstance(instance.getGroup(), instance.getUri());
                }
            });
        }
    }
}
