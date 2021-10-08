package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.config.Condition;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.context.Request;


public interface HttpSelector {


    boolean test(Request request, Condition condition);


    String select(Exchange exchange, String name);

}
