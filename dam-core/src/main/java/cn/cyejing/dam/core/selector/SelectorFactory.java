
package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.enums.EnumType;
import cn.cyejing.dam.common.expression.EvaluationContext;
import cn.cyejing.dam.core.context.Request;
import com.google.common.collect.Maps;

import java.util.Map;


public class SelectorFactory {

    private final Map<EnumType, HttpSelector> httpSelectorMap = Maps.newHashMap();

    private static final SelectorFactory INSTANCE = new SelectorFactory();

    private SelectorFactory() {
        httpSelectorMap.put(EnumType.HEADER, new HeaderSelector());
        httpSelectorMap.put(EnumType.PATH, new PathSelector());
        httpSelectorMap.put(EnumType.METHOD, new MethodSelector());
        httpSelectorMap.put(EnumType.COOKIE, new CookieSelector());
        httpSelectorMap.put(EnumType.HOST, new HostSelector());
        httpSelectorMap.put(EnumType.IP, new IpSelector());
        httpSelectorMap.put(EnumType.QUERY, new QuerySelector());
        httpSelectorMap.put(EnumType.BODY, new BodySelector());
        httpSelectorMap.put(EnumType.CONTEXT, new ContextSelector());
    }


    public static HttpSelector getHttpSelector(EnumType type) {
        return INSTANCE.httpSelectorMap.get(type);
    }


    public static EvaluationContext buildContext(Request request) {
        return new EvaluationContext().conditionPredicate(condition -> getHttpSelector(condition.getType()).test(request, condition));
    }
}
