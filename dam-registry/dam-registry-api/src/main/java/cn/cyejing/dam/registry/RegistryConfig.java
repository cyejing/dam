package cn.cyejing.dam.registry;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class RegistryConfig {
    @Getter
    private String addresses;
    @Getter
    private String namespace;
    @Getter
    private List<Runnable> reconnectCallbacks = new ArrayList<>();


    public RegistryConfig(String namespace, String addresses) {
        this.addresses = addresses;
        this.namespace = namespace;
    }

    public void addReconnectCallback(Runnable run) {
        reconnectCallbacks.add(run);
    }

}
