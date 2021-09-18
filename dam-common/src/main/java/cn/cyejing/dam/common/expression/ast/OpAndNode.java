
package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.expression.EvaluationContext;


public class OpAndNode extends OperatorNode {
    public OpAndNode(Node... operands) {
        super("and", operands);
    }

    @Override
    public Object evaluate(EvaluationContext evaluationContext) {
        if (!(Boolean) getLeftNode().evaluate(evaluationContext)) {
            return Boolean.FALSE;
        }
        return getRightNode().evaluate(evaluationContext);
    }
}
