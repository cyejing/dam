
package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.module.Condition;
import cn.cyejing.dam.core.context.AttributeKey;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.context.Request;
import cn.cyejing.dam.core.selector.match.Match;


public class ContextSelector extends AbstractHttpSelector {

    @Override
    public boolean doTest(Request request, Condition condition, Match matchMatch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String select(Exchange exchange, String name) {
        Object value = exchange.getAttribute(AttributeKey.valueOf(name));
        return value != null ? value.toString() : null;
    }
}
