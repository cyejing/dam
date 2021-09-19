package cn.cyejing.dam.common.expression.token;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;


@Data
public class Token {

    private String expr;

    private TokenType type;

    private String key;

    private String data;

    private int startPos;

    private int endPos;

    Token(TokenType tokenType, int startPos, int endPos) {
        this.type = tokenType;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    Token(TokenType tokenType, String key, String data, int startPos, int endPos) {
        this(tokenType, startPos, endPos);
        this.key = key;
        this.data = data;
    }

    public TokenType getType() {
        return this.type;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[").append(this.type.toString());
        if (StringUtils.isNotEmpty(this.key)) {
            s.append(":").append(this.key);
        }
        if (StringUtils.isNotEmpty(this.data)) {
            s.append(":").append(this.data);
        }
        s.append("]");
        s.append("(").append(this.startPos).append(",").append(this.endPos).append(")");
        return s.toString();
    }
}
