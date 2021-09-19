package cn.cyejing.dam.common.expression;

import cn.cyejing.dam.common.expression.ast.Node;


public class DefaultExpressionImpl implements Expression {

    private final String expressionStr;
    private final Node ast;

    public DefaultExpressionImpl(String expressionStr, Node ast) {
        this.expressionStr = expressionStr;
        this.ast = ast;
    }


    @Override
    public Object evaluate(EvaluationContext evaluationContext) {
        return ast.evaluate(evaluationContext);
    }

    @Override
    public String toStringAST() {
        return ast.toStringAST();
    }


    public String getExpressionStr() {
        return expressionStr;
    }

}
