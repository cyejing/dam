
package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.expression.EvaluationContext;


public class NotNode extends Node {

    public NotNode(Node... operands) {
        super(operands);
    }

    @Override
    public Object evaluate(EvaluationContext evaluationContext) {
        return !(Boolean) getChild(0).evaluate(evaluationContext);
    }

    @Override
    public String toStringAST() {
        return "!" + getChild(0).toStringAST();
    }
}
