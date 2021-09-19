
package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;


public class SymbolTokenReader implements TokenReader {
    @Override
    public boolean check(TokenStream tokenStream) {
        char c = tokenStream.peekChar();
        return c == '.' || c == '(' || c == ')' ||
                c == '[' || c == ']' || c == ';';
    }

    @Override
    public void process(TokenStream tokenStream) {
        char c = tokenStream.peekChar();
        if (c == '.') {
            tokenStream.addToken(TokenType.DOT, ".");
        } else if (c == '(') {
            tokenStream.addToken(TokenType.LPAREN, "(");
        } else if (c == ')') {
            tokenStream.addToken(TokenType.RPAREN, ")");
        } else if (c == '[') {
            tokenStream.addToken(TokenType.LSQUARE, "[");
        } else if (c == ']') {
            tokenStream.addToken(TokenType.RSQUARE, "]");
        } else if (c == ';') {
            tokenStream.addToken(TokenType.SEMICOLON, ";");
        }
    }
}
