package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.expression.EvaluationContext;


public class OpForwardNode extends Node {

    private String address;

    public OpForwardNode(String address, Node... operands) {
        super(operands);
        this.address = address;
    }

    @Override
    public Object evaluate(EvaluationContext evaluationContext) {
        Object res = getChild(0).evaluate(evaluationContext);
        if (res instanceof Boolean && (Boolean) res) {
            return address;
        }
        return null;
    }

    @Override
    public String toStringAST() {
        return getChild(0).toStringAST() + " => " + address + ";";
    }
}
