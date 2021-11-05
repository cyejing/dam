package cn.cyejing.dam.core.filter.loadbalance;

import java.util.HashMap;
import java.util.Map;

public class LoadBalanceFactory {

    private final Map<String, LoadBalance> loadBalanceMap = new HashMap<>();
    private static final LoadBalanceFactory INSTANCE = new LoadBalanceFactory();

    private LoadBalanceFactory() {
        loadBalanceMap.put("rlb", new RandomLoadBalance());
    }

    public static LoadBalance getLoadBalance(String loadBalance) {
        return INSTANCE.loadBalanceMap.get(loadBalance);
    }

}
