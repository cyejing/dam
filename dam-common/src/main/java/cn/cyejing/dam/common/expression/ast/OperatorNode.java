
package cn.cyejing.dam.common.expression.ast;


public abstract class OperatorNode extends Node {

    private final String operatorName;

    public OperatorNode(String operatorName, Node... operands) {
        super(operands);
        this.operatorName = operatorName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    protected Node getLeftNode() {
        return this.children[0];
    }

    protected Node getRightNode() {
        return this.children[1];
    }

    @Override
    public String toStringAST() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(getChild(0).toStringAST());
        for (int i = 1; i < getChildCount(); i++) {
            sb.append(" ").append(getOperatorName()).append(" ");
            sb.append(getChild(i).toStringAST());
        }
        sb.append(")");
        return sb.toString();
    }
}
