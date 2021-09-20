package cn.cyejing.dam.common.module;

import cn.cyejing.dam.common.expression.ExpressionParser;
import cn.cyejing.dam.common.utils.JSONUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;


@Slf4j
public class DefaultDynamicConfig implements DynamicConfig {

    private static final DynamicConfig INSTANCE = new DefaultDynamicConfig();

    @Getter
    private ConcurrentMap<String/*group*/, SortedSet<Route>> routsMap = new ConcurrentHashMap<>();


    @Getter
    private ConcurrentMap<String/*host*/, Set<Instance>> instanceMap = new ConcurrentHashMap<>();


    @Getter
    private Set<Route> defaultRoutes = new ConcurrentSkipListSet<>();


    private DefaultDynamicConfig() {
    }

    public static DynamicConfig getInstance() {
        return INSTANCE;
    }


    @Override
    public Route getRoute(String group, String id) {
        return getRoutes(group).stream().filter(route -> route.getId().equals(id)).findFirst().orElseGet(null);
    }

    @Override
    public SortedSet<Route> getRoutes(String group) {
        SortedSet<Route> routes = routsMap.get(group);
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
        if (route.isGlobal()) {
            defaultRoutes.add(route);
        }
        String group = route.getGroup();
        if (StringUtils.isEmpty(group)) {
            throw new IllegalArgumentException("group can not be empty. id: " + route.getId());
        }
        SortedSet<Route> routes = routsMap.get(group);
        if (routes == null) {
            routsMap.putIfAbsent(group, new ConcurrentSkipListSet<>());
            routes = routsMap.get(group);
        }
        if (routes.removeIf(r -> r.getId().equals(route.getId()))) {
            log.info("modify Route group:{}, id:{}, route:{}", route.getGroup(), route.getId(), JSONUtil.writeValueAsString(route));
        } else {
            log.info("add Route group:{}, id:{}, route:{}", route.getGroup(), route.getId(), JSONUtil.writeValueAsString(route));
        }
        routes.add(route);
    }


    @Override
    public void deleteRoute(String group, String id) {
        log.info("delete Route group:{}, id:{}", group, id);
        SortedSet<Route> routes = routsMap.get(group);
        if (routes != null) {
            routes.removeIf(route -> route.getId().equals(id));
        }
        defaultRoutes.removeIf(route -> route.getId().equals(id));
    }


    @Override
    public void addInstance(Instance instance) {
        String host = instance.getHost();
        if (StringUtils.isEmpty(host)) {
            throw new IllegalArgumentException("host can not be empty");
        }
        Set<Instance> instances = instanceMap.get(host);
        if (instances == null) {
            instanceMap.putIfAbsent(host, ConcurrentHashMap.newKeySet());
            instances = instanceMap.get(host);
        }
        if (instances.removeIf(ins -> ins.getAddress().equals(instance.getAddress()))) {
            log.info("modify Instance host:{}, address:{}, instance:{}", instance.getHost(), instance.getAddress(), JSONUtil.writeValueAsString(instance));
        } else {
            log.info("add Instance host:{}, address:{}, instance:{}", instance.getHost(), instance.getAddress(), JSONUtil.writeValueAsString(instance));
        }
        instances.add(instance);
    }

    @Override
    public void deleteInstance(String host, String address) {
        log.info("delete Instance host:{}, address:{}", host, address);
        Set<Instance> instances = instanceMap.get(host);
        instances.removeIf(instance -> instance.getAddress().equals(address));
    }


    @Override
    public Set<Instance> getInstances(String host) {
        if (instanceMap.get(host) == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(instanceMap.get(host));
    }


    @Override
    public Set<Instance> getInstances(String host,String tag) {
        if (instanceMap.get(host) == null) {
            return Collections.emptySet();
        }
        return instanceMap.get(host).stream()
                .filter(instance -> instance.getTags().contains(tag))
                .collect(Collectors.toSet());
    }


    @Override
    public void clear() {
        log.info("clean dynamic config");
        routsMap.clear();
        instanceMap.clear();
        defaultRoutes.clear();
    }

}
