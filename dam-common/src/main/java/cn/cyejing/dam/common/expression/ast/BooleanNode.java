package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.expression.EvaluationContext;


public class BooleanNode extends Node {
    private String originalValue;

    public BooleanNode(String originalValue, Node... operands) {
        super(operands);
        this.originalValue = originalValue;
    }

    @Override
    public Object evaluate(EvaluationContext evaluationContext) {
        return Boolean.valueOf(originalValue);
    }

    @Override
    public String toStringAST() {
        return toString();
    }

    @Override
    public String toString() {
        return originalValue;
    }
}
