package cn.cyejing.dam.core.filter.loadbalance;


import cn.cyejing.dam.common.module.Instance;
import cn.cyejing.dam.core.context.Exchange;

import java.util.Set;


public interface LoadBalance {

    Instance select(Exchange exchange, Set<Instance> instances);
}
