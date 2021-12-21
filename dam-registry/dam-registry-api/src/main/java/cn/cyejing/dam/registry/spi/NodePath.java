package cn.cyejing.dam.registry.spi;

import cn.cyejing.dam.common.config.Instance;
import cn.cyejing.dam.common.config.Route;

/**
 * @author chenyejing
 */
public class NodePath {

    private static final String INSTANCE = "/instance";
    private static final String ROUTE = "/route";

    private String root;

    private NodePath(String root) {
        this.root = root;
    }

    public static NodePath root(String root) {
        return new NodePath("/" + root);
    }

    public String genKey() {
        return this.root;
    }

    public String genInstanceKey(Instance instance) {
        return this.root + INSTANCE + "/" + instance.getGroup() + "/" + instance.getUri();
    }

    public String genInstanceKey(String group, String uri) {
        return this.root + INSTANCE + "/" + group + "/" + uri;
    }

    public String genInstanceKey(String group) {
        return this.root + INSTANCE + "/" + group + "/";
    }

    public String genInstanceKey() {
        return this.root + INSTANCE + "/";
    }

    public String genRouteKey(Route route) {
        return this.root + ROUTE + "/" + route.getGroup() + "/" + route.getId();
    }


    public String genRouteKey(String group, String routeId) {
        return this.root + ROUTE + "/" + group + "/" + routeId;
    }

    public String genRouteKey(String group) {
        return this.root + ROUTE + "/" + group + "/";
    }

    public String genRouteKey() {
        return this.root + ROUTE + "/";
    }
}
