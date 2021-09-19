/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package cn.cyejing.dam.core.filter.loadbalance;


import cn.cyejing.dam.common.module.Instance;
import cn.cyejing.dam.core.context.Exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <B>主类名称：负载均衡基础类,提高权重计算方法</B><BR>
 * <B>概要说明：</B><BR>
 *
 * @author Born
 * @version : AbstractLoadBalance.java,v 0.1 2020年06月05日 2:17 下午
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    private static final int DEFAULT_WEIGHT = 100;
    private static final int DEFAULT_WARMUP = 5 * 60 * 1000;;

    @Override
    public Instance select(Exchange exchange, Set<Instance> sets){
        if (sets == null || sets.size() == 0) {
            return null;
        }

        List<Instance> instances = new ArrayList<>(sets);

        if (instances.size() == 1) {
            return instances.get(0);
        }

        Instance instance = doSelect(exchange, instances);
        return instance;
    }

    protected abstract Instance doSelect(Exchange exchange, List<Instance> instances);


    protected static int getWeight(Instance instance) {
        int weight = instance.getWeight() == null ? DEFAULT_WEIGHT : instance.getWeight();
        if (weight > 0) {
            long timestamp = instance.getRegisterTime();
            if (timestamp > 0L) {
                int uptime = (int) (System.currentTimeMillis() - timestamp);
                int warmup = DEFAULT_WARMUP;
                if (uptime > 0 && uptime < warmup) {
                    weight = calculateWarmUpWeight(uptime, warmup, weight);
                }
            }
        }
        return weight;
    }


    static int calculateWarmUpWeight(int uptime, int warmUp, int weight) {
        int ww = (int) ((float) uptime / ((float) warmUp / (float) weight));
        return ww < 1 ? 1 : (ww > weight ? weight : ww);
    }

}
