package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.module.Condition;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.context.Request;
import cn.cyejing.dam.core.selector.match.Match;


public class IpSelector extends AbstractHttpSelector {


    @Override
    public boolean doTest(Request request, Condition condition, Match matchMatch) {
        return matchMatch.match(condition.getValue(), request.getClientIp());
    }

    @Override
    public String select(Exchange exchange, String name) {
        return exchange.getRequest().getClientIp();
    }
}
