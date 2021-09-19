package cn.cyejing.dam.core.support.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheManager {

    private final ConcurrentMap<String /* filterName */, Cache> routeCacheMap = new ConcurrentHashMap<>();

    private static final CacheManager INSTANCE = new CacheManager();

    private CacheManager() {
    }


    public static <V> Cache<String, V> createForRoute(String routeId) {
        return INSTANCE.routeCacheMap.computeIfAbsent(routeId, s -> Caffeine.newBuilder().build());
    }

    public static void removeForRoute(String routeId) {
        INSTANCE.routeCacheMap.remove(routeId);
    }

    public static void cleanAllForRoute() {
        INSTANCE.routeCacheMap.values().forEach(cache -> cache.invalidateAll());
    }

}
