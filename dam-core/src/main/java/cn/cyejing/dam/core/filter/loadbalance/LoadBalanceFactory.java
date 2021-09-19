/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package cn.cyejing.dam.core.filter.loadbalance;


import cn.cyejing.dam.common.enums.EnumLoadBalance;

import java.util.HashMap;
import java.util.Map;

/**
 * <B>主类名称：</B><BR>
 * <B>概要说明：</B><BR>
 *
 * @author Born
 * @version : LoadBalanceFactory.java,v 0.1 2020年06月05日 4:27 下午
 */
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
