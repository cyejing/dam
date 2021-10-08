package cn.cyejing.dam.core.filter.loadbalance;


import cn.cyejing.dam.common.config.Instance;
import cn.cyejing.dam.core.context.Exchange;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected Instance doSelect(Exchange exchange, List<Instance> instances) {
        int length = instances.size();
        int totalWeight = 0;
        boolean sameWeight = true;
        for (int i = 0; i < length; i++) {
            int weight = getWeight(instances.get(i));
            totalWeight += weight;
            if (sameWeight && i > 0 && weight != getWeight(instances.get(i - 1))) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            for (Instance instance : instances) {
                offset -= getWeight(instance);
                if (offset < 0) {
                    return instance;
                }
            }
        }
        return instances.get(ThreadLocalRandom.current().nextInt(length));
    }

}
