package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.config.Condition;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.context.Request;
import cn.cyejing.dam.core.selector.match.Match;

public class ContextSelector extends AbstractHttpSelector {

    @Override
    public boolean doTest(Request request, Condition condition, Match match) {
        return false;
    }

    @Override
    public String select(Exchange exchange, String name) {
        return null;
    }
}
