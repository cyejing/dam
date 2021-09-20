package cn.cyejing.dam.core.filter.loadbalance;
import cn.cyejing.dam.common.enums.EnumLoadBalance;

import java.util.HashMap;
import java.util.Map;

public class LoadBalanceFactory {

    private final Map<EnumLoadBalance, LoadBalance> loadBalanceMap = new HashMap<>();
    private static final LoadBalanceFactory INSTANCE = new LoadBalanceFactory();

    private LoadBalanceFactory() {
        loadBalanceMap.put(EnumLoadBalance.RANDOM, new RandomLoadBalance());
    }

    public static LoadBalance getLoadBalance(EnumLoadBalance loadBalance) {
        return INSTANCE.loadBalanceMap.get(loadBalance);
    }

}
