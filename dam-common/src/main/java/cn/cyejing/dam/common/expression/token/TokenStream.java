package cn.cyejing.dam.common.expression.token;

import java.util.ArrayList;
import java.util.List;


public class TokenStream {

    private final String expressionString;
    private final int max;
    private final List<Token> tokens = new ArrayList<>();
    private int pos;

    public TokenStream(String inputData) {
        this.expressionString = inputData + "\0";
        this.max = this.expressionString.length();
        this.pos = 0;
    }

    public int getMax() {
        return this.max;
    }

    public boolean isEnd() {
        return this.pos >= this.max;
    }

    public List<Token> getTokens() {
        return this.tokens;
    }

    public int getPos() {
        return pos;
    }

    public char peekChar() {
        return expressionString.charAt(this.pos);
    }

    public char peekChar(int pos) {
        return expressionString.charAt(pos);
    }

    public String peekPairChar() {
        return expressionString.substring(this.pos, this.pos + 2);
    }

    public String peekString(String... strArray) {
        for (String s : strArray) {
            if (peekStringSingle(s)) {
                return s;
            }
        }
        return null;
    }

    private boolean peekStringSingle(String s) {
        if (this.max - this.pos < s.length()) {
            return false;
        }
        return expressionString.startsWith(s, this.pos);
    }


    public void addToken(TokenType type, String data) {
        addToken(type, null, data);
    }

    public void addToken(TokenType type, int start, int pos) {
        addToken(type, null, start, pos);
    }

    public void addToken(TokenType type, String key, String data) {
        this.tokens.add(new Token(type, key, data, pos, pos + data.length()));
        this.pos += data.length();
    }

    public void addToken(TokenType type, String key, int start, int pos) {
        String subStr;
        if (type == TokenType.LITERAL_STRING) {
            subStr = expressionString.substring(start + 1, pos - 1);
        } else {
            subStr = expressionString.substring(start, pos);
        }
        this.tokens.add(new Token(type, key, subStr, start, pos));
        this.pos = pos;
    }

    public void skipPos() {
        this.pos++;
    }

}
