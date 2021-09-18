
package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.expression.EvaluationContext;


public class OpOrNode extends OperatorNode {
    public OpOrNode(Node... operands) {
        super("or", operands);
    }

    @Override
    public Object evaluate(EvaluationContext evaluationContext) {
        if ((Boolean) getLeftNode().evaluate(evaluationContext)) {
            return Boolean.TRUE;
        }
        return getRightNode().evaluate(evaluationContext);
    }
}
