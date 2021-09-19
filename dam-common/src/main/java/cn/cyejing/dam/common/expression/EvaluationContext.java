package cn.cyejing.dam.common.expression;

import cn.cyejing.dam.common.module.Condition;

import java.util.function.Predicate;


public class EvaluationContext {

    private Predicate<Condition> conditionPredicate;

    public Predicate<Condition> getConditionPredicate() {
        return conditionPredicate;
    }

    public EvaluationContext conditionPredicate(Predicate<Condition> conditionPredicate) {
        this.conditionPredicate = conditionPredicate;
        return this;
    }

}
