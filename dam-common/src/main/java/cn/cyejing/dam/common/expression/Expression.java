package cn.cyejing.dam.common.expression;


public interface Expression {

    Object evaluate(EvaluationContext evaluationContext);

    String toStringAST();

    default boolean evaluateBoolean(EvaluationContext evaluationContext) {
        return (Boolean) evaluate(evaluationContext);
    }

    default String evaluateString(EvaluationContext evaluationContext) {
        return (String) evaluate(evaluationContext);
    }
}
