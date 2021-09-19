package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.enums.EnumOperator;
import cn.cyejing.dam.common.module.Condition;
import cn.cyejing.dam.core.context.Request;
import lombok.Getter;

import java.util.function.Predicate;


public class PredicateSelector {

    @Getter
    private final EnumOperator operator;
    private HttpSelector httpSelector;
    private Request request;
    private Condition condition;
    private Predicate<?> predicate;


    public PredicateSelector(Request request, Condition condition) {
        this.httpSelector = SelectorFactory.getHttpSelector(condition.getType());
        this.request = request;
        this.condition = condition;
        this.operator = condition.getOperator();
    }


    public PredicateSelector(Predicate<?> predicate, EnumOperator operator) {
        this.predicate = predicate;
        this.operator = operator;

    }


    public boolean test() {
        if (predicate != null) {
            return predicate.test(null);
        } else {
            return httpSelector.test(request, condition);
        }
    }


    public PredicateSelector operator(PredicateSelector predicateSelector, EnumOperator operator) {
        switch (operator) {
            case AND:
                return new PredicateSelector(o -> this.test() && predicateSelector.test(), operator);
            case OR:
                return new PredicateSelector(o -> this.test() || predicateSelector.test(), operator);
            case NEGATE:
                return new PredicateSelector(o -> !this.test(), operator);
            default:
                return this;
        }
    }

}
