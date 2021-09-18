package cn.cyejing.dam.registry.service;

import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.RegistryService;
import cn.cyejing.dam.registry.api.InstanceAPI;
import cn.cyejing.dam.registry.api.RouteAPI;
import cn.cyejing.dam.registry.spi.RegistrySPI;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class RegistryServiceImpl extends AbstractService implements RegistryService {

    protected RegistryConfig config;
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private InstanceServiceImpl instanceAPI;
    private RouteServiceImpl routeAPI;

    public RegistryServiceImpl(RegistrySPI registrySPI) {
        super(registrySPI);
        this.routeAPI = new RouteServiceImpl(registrySPI);
        this.instanceAPI = new InstanceServiceImpl(registrySPI);
    }

    @Override
    public void initialize(RegistryConfig config) {
        initialize(config, true);
    }

    @Override
    public void initialize(RegistryConfig config, boolean watch) {
        if (initialized.compareAndSet(false, true)) {
            log.info("initialize registry config:{}", JSONUtil.toJSONString(config));
            this.config = config;
            getRegistrySPI().init(config);
            if (watch) {
                watch();
            }
        }
    }


    private void watch() {
        instanceAPI.watchInstance();
        routeAPI.watchRoute();
    }

    @Override
    public InstanceAPI getInstanceAPI() {
        return instanceAPI;
    }

    @Override
    public RouteAPI getRouteAPI() {
        return routeAPI;
    }

}
