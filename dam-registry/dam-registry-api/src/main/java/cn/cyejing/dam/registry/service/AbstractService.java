package cn.cyejing.dam.registry.service;

import cn.cyejing.dam.registry.spi.NodePath;
import cn.cyejing.dam.registry.spi.RegistrySPI;


public abstract class AbstractService {

    private final RegistrySPI registrySPI;

    public AbstractService(RegistrySPI registrySPI) {
        this.registrySPI = registrySPI;
    }

    public RegistrySPI getRegistrySPI() {
        return registrySPI;
    }

    public String getNamespace() {
        return registrySPI.getNamespace();
    }

    public NodePath getNodePath() {
        return NodePath.root(getNamespace());
    }
}
