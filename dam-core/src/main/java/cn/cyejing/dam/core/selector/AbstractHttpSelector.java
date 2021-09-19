package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.module.Condition;
import cn.cyejing.dam.core.context.Request;
import cn.cyejing.dam.core.selector.match.Match;
import cn.cyejing.dam.core.selector.match.MatchFactory;


public abstract class AbstractHttpSelector implements HttpSelector {

    @Override
    public boolean test(Request request, Condition condition) {
        return doTest(request, condition, MatchFactory.getMatch(condition.getMatch()));
    }

    public abstract boolean doTest(Request request, Condition condition, Match match);
}
