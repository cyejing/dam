package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.expression.EvaluationContext;
import cn.cyejing.dam.common.utils.Assert;


public abstract class Node {

    private static final Node[] NO_CHILDREN = new Node[0];

    protected Node[] children = Node.NO_CHILDREN;

    protected Node parent;


    public Node(Node... operands) {
        if (operands != null && operands.length > 0) {
            this.children = operands;
            for (Node operand : operands) {
                Assert.notNull(operand, "Child nodes must not be empty");
                operand.parent = this;
            }
        }
    }

    public Node[] getChildren() {
        return this.children;
    }

    public Node getChild(int index) {
        return this.children[index];
    }


    public int getChildCount() {
        return this.children.length;
    }

    public abstract Object evaluate(EvaluationContext evaluationContext);

    public abstract String toStringAST();
}
