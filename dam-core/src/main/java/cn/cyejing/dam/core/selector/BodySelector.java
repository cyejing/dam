package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.config.Condition;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.context.Request;
import cn.cyejing.dam.core.selector.match.Match;


public class BodySelector extends AbstractHttpSelector {


    @Override
    public boolean doTest(Request request, Condition condition, Match matchMatch) {
        return matchMatch.match(condition.getValue(), request.getPostParameters(condition.getName()));
    }

    @Override
    public String select(Exchange exchange, String name) {
        return exchange.getRequest().getPostParameters(name);
    }
}
