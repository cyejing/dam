package cn.cyejing.dam.registry;

import lombok.Getter;

public class RegistryConfig {
    @Getter
    private String urls;
    @Getter
    private String namespace;

    public RegistryConfig(String namespace, String urls) {
        this.urls = urls;
        this.namespace = namespace;
    }

}
