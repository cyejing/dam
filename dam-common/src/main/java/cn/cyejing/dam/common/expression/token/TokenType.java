
package cn.cyejing.dam.common.expression.token;


public enum TokenType {

    LITERAL_STRING("literalString"),

    IDENTIFIER,

    BOOLEAN_TYPE("booleanType"),

    CONDITION_TYPE("conditionType"),

    CONDITION_TYPE_NAME("conditionTypeName"),

    CONDITION_OPERATE("conditionOperate"),

    LPAREN("("),

    RPAREN(")"),

    LSQUARE("["),

    RSQUARE("]"),

    DOT("."),

    NOT("!"),

    SYMBOLIC_OR("||"),

    SYMBOLIC_AND("&&"),

    FORWARD("=>"),

    SEMICOLON(";")

    ;

    private String tokenStr;
    private final boolean hasPayload;


    TokenType(String tokenStr) {
        this.tokenStr = tokenStr;
        this.hasPayload = (tokenStr.length() != 0);
    }

    public String getTokenStr() {
        return tokenStr;
    }

    TokenType() {
        this("");
    }


    @Override
    public String toString() {
        return name();
    }

    public boolean hasPayload() {
        return this.hasPayload;
    }

}
