/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package cn.cyejing.dam.core.filter.loadbalance;


import cn.cyejing.dam.common.module.Instance;
import cn.cyejing.dam.core.context.Exchange;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <B>主类名称：随机负载均衡算法</B><BR>
 * <B>概要说明：</B><BR>
 *
 * @author Born
 * @version : RandomLoadBalance.java,v 0.1 2020年06月05日 4:17 下午
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected Instance doSelect(Exchange exchange, List<Instance> instances) {
        int length = instances.size();
        int totalWeight = 0;
        boolean sameWeight = true; // 	是否每个实例的权重都相同
        for (int i = 0; i < length; i++) {
            int weight = getWeight(instances.get(i));
            totalWeight += weight; // 	计算总的权重
            if (sameWeight && i > 0 && weight != getWeight(instances.get(i - 1))) {
                sameWeight = false; // 	有权重不一样的实例,走权重的逻辑
            }
        }
        if (totalWeight > 0 && !sameWeight) { // 	权重不一致的逻辑
            // 	根据总权重随机出一个偏移量
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            // 	根据偏移量找到靠近偏移量的实例
            for (Instance instance : instances) {
                offset -= getWeight(instance);
                if (offset < 0) {
                    return instance;
                }
            }
        }
        // 	如果所有实例权重一致,使用随机出一个实例即可
        return instances.get(ThreadLocalRandom.current().nextInt(length));
    }

}
