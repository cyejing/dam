package cn.cyejing.dam.common.module;

import cn.cyejing.dam.common.expression.ExpressionParser;
import cn.cyejing.dam.common.utils.JSONUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;


@Slf4j
public class DefaultDynamicConfig implements DynamicConfig {

    private static final DynamicConfig INSTANCE = new DefaultDynamicConfig();

    @Getter
    private ConcurrentMap<String/*serviceName*/, SortedSet<Route>> routsMap = new ConcurrentHashMap<>();


    @Getter
    private ConcurrentMap<String/*serviceName*/, Set<Instance>> instanceMap = new ConcurrentHashMap<>();


    @Getter
    private Set<Route> defaultRoutes = new ConcurrentSkipListSet<>();


    private DefaultDynamicConfig() {
    }

    public static DynamicConfig getInstance() {
        return INSTANCE;
    }


    @Override
    public SortedSet<Route> getRoutes(String serviceName) {
        SortedSet<Route> routes = routsMap.get(serviceName);
        if (routes == null) {
            return null;
        }
        return Collections.unmodifiableSortedSet(routes);
    }


    @Override
    public void addRoute(Route route) {
        if (StringUtils.isNotEmpty(route.getExpressionStr())) {
            route.setExpression(ExpressionParser.parse(route.getExpressionStr()));
        }

        defaultRoutes.removeIf(r -> r.getId().equals(route.getId()));
        if (route.isDefaultRoute()) {
            defaultRoutes.add(route);
        }
        String serviceName = route.getServiceName();
        if (StringUtils.isEmpty(serviceName)) {
            throw new IllegalArgumentException("serviceName can not be empty. id: " + route.getId());
        }
        SortedSet<Route> routes = routsMap.get(serviceName);
        if (routes == null) {
            routsMap.putIfAbsent(serviceName, new ConcurrentSkipListSet<>());
            routes = routsMap.get(serviceName);
        }
        if (routes.removeIf(r -> r.getId().equals(route.getId()))) {
            log.info("modify Route serviceName:{}, id:{}, route:{}", route.getServiceName(), route.getId(), JSONUtil.toJSONString(route));
        } else {
            log.info("add Route serviceName:{}, id:{}, route:{}", route.getServiceName(), route.getId(), JSONUtil.toJSONString(route));
        }
        routes.add(route);
    }


    @Override
    public void deleteRoute(String serviceName, String id) {
        log.info("delete Route serviceName:{}, id:{}", serviceName, id);
        SortedSet<Route> routes = routsMap.get(serviceName);
        if (routes != null) {
            routes.removeIf(route -> route.getId().equals(id));
        }
        defaultRoutes.removeIf(route -> route.getId().equals(id));
    }


    @Override
    public void addInstance(Instance instance) {
        String serviceName = instance.getServiceName();
        if (StringUtils.isEmpty(serviceName)) {
            throw new IllegalArgumentException("serviceName can not be empty");
        }
        Set<Instance> instances = instanceMap.get(serviceName);
        if (instances == null) {
            instanceMap.putIfAbsent(serviceName, ConcurrentHashMap.newKeySet());
            instances = instanceMap.get(serviceName);
        }
        if (instances.removeIf(ins -> ins.getAddress().equals(instance.getAddress()))) {
            log.info("modify Instance serviceName:{}, address:{}, instance:{}", instance.getServiceName(), instance.getAddress(), JSONUtil.toJSONString(instance));
        } else {
            log.info("add Instance serviceName:{}, address:{}, instance:{}", instance.getServiceName(), instance.getAddress(), JSONUtil.toJSONString(instance));
        }
        instances.add(instance);
    }

    @Override
    public void deleteInstance(String serviceName, String address) {
        log.info("delete Instance serviceName:{}, address:{}", serviceName, address);
        Set<Instance> instances = instanceMap.get(serviceName);
        instances.removeIf(instance -> instance.getAddress().equals(address));
    }


    @Override
    public Set<Instance> getInstancesNormal(String serviceName) {
        if (instanceMap.get(serviceName) == null) {
            return Collections.emptySet();
        }

        return Collections.unmodifiableSet(instanceMap.get(serviceName));
    }


    @Override
    public Set<Instance> getInstancesAll(String serviceName) {
        return instanceMap.get(serviceName) == null ? new HashSet<>() : Collections.unmodifiableSet(instanceMap.get(serviceName));
    }


    @Override
    public void clear() {
        log.info("clean dynamic config");
        routsMap.clear();
        instanceMap.clear();
        defaultRoutes.clear();
    }

}
