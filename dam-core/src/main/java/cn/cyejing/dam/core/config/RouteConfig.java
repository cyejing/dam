package cn.cyejing.dam.core.config;

import cn.cyejing.dam.common.module.Instance;
import cn.cyejing.dam.common.module.Route;
import lombok.Data;

import java.util.List;

@Data
public class RouteConfig {

    public RouteConfig() {
    }

    private List<Route> routes;

    private List<Instance> instances;
}
