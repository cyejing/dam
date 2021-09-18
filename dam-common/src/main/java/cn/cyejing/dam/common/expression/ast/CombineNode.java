
package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.expression.EvaluationContext;


public class CombineNode extends Node{
    public CombineNode(Node... operands) {
        super(operands);
    }

    @Override
    public Object evaluate(EvaluationContext evaluationContext) {
        for (Node child : getChildren()) {
            Object res = child.evaluate(evaluationContext);
            if (res != null) {
                return res;
            }
        }
        return null;
    }



    @Override
    public String toStringAST() {
        StringBuilder sb = new StringBuilder();
        for (Node child : getChildren()) {
            sb.append(child.toStringAST()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
