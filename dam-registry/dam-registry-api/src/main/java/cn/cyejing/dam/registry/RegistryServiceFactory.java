package cn.cyejing.dam.registry;

import cn.cyejing.dam.registry.service.RegistryServiceImpl;
import cn.cyejing.dam.registry.spi.RegistrySPI;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;


public class RegistryServiceFactory {

    private static final RegistryServiceFactory INSTANCE = new RegistryServiceFactory();
    private RegistryService registryService;
    private RegistrySPI registrySPI;


    private RegistryServiceFactory() {
        ServiceLoader<RegistrySPI> serviceLoader = ServiceLoader.load(RegistrySPI.class);
        this.registrySPI = StreamSupport.stream(serviceLoader.spliterator(), false).findFirst().get();
        this.registryService = new RegistryServiceImpl(registrySPI);
    }

    public static RegistryService getRegistryService() {
        return INSTANCE.registryService;
    }

}
