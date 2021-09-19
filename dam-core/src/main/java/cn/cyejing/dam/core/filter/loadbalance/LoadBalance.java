/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package cn.cyejing.dam.core.filter.loadbalance;


import cn.cyejing.dam.common.module.Instance;
import cn.cyejing.dam.core.context.Exchange;

import java.util.List;
import java.util.Set;

/**
 * <B>主类名称：</B><BR>
 * <B>概要说明：</B><BR>
 *
 * @author Born
 * @version : LoadBalance.java,v 0.1 2020年06月05日 2:16 下午
 */
public interface LoadBalance {

    Instance select(Exchange exchange, Set<Instance> instances);
}
