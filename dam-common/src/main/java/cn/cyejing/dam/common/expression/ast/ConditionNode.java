
package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.module.Condition;
import cn.cyejing.dam.common.expression.EvaluationContext;
import cn.cyejing.dam.common.expression.EvaluationException;


public class ConditionNode extends Node {
    private final String type;
    private final String name;
    private final String operate;
    private final String value;

    private Condition condition;

    public ConditionNode(String type, String name, String operate, String value, Node... operands) {
        super(operands);
        this.type = type;
        this.name = name;
        this.operate = operate;
        this.value = value;
        this.condition = new Condition(type, name, operate, value);
    }

    @Override
    public Object evaluate(EvaluationContext evaluationContext) {
        if (evaluationContext == null || evaluationContext.getConditionPredicate() == null) {
            throw new EvaluationException("The conditional predicate is not set");
        }
        return evaluationContext.getConditionPredicate().test(this.condition);
    }

    @Override
    public String toStringAST() {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        if (name != null) {
            sb.append("['" + name + "']");
        }
        sb.append('.');
        sb.append(operate);
        sb.append("('" + value + "')");
        return sb.toString();
    }
}
