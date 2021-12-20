package cn.cyejing.dam.registry.memory;

import cn.cyejing.dam.common.config.Instance;
import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.spi.NodePath;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author chenyejing
 */
@Slf4j
public class FileConfigLoad {
    private MemoryRegistry memoryRegistry;
    private RegistryConfig registryConfig;

    public FileConfigLoad(MemoryRegistry memoryRegistry, RegistryConfig config) {
        this.memoryRegistry = memoryRegistry;
        this.registryConfig = config;
    }

    public void load() {
        loadFile(registryConfig.getUrls());
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
            inputStream = FileConfigLoad.class.getClassLoader().getResourceAsStream(path);
            log.info("load route yaml classpath: {}", path);
        } else {
            log.info("load route yaml path: {}", path);
        }
        return loadFile(inputStream);
    }

    public boolean loadFile(InputStream inputStream) {
        if (inputStream == null) {
            throw new RuntimeException("route yaml is not exist.");
        }

        try {
            Yaml yaml = new Yaml();
            RouteConfig config = yaml.loadAs(inputStream, RouteConfig.class);
            for (Route route : config.getRoutes()) {
                memoryRegistry.put(NodePath.root(registryConfig.getNamespace()).genRouteKey(route),
                        JSONUtil.writeValueAsString(route));
            }
            for (Instance instance : config.getInstances()) {
                memoryRegistry.put(NodePath.root(registryConfig.getNamespace()).genInstanceKey(instance), JSONUtil.writeValueAsString(instance));
            }
            log.info("reload route yaml success.");
            return true;
        } catch (Throwable t) {
            log.error("reload route yaml failed", t);
            return false;
        }
    }
}
