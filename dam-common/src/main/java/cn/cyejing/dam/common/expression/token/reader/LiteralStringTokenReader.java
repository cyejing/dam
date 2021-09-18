
package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.expression.ExpressionException;
import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;


public class LiteralStringTokenReader implements TokenReader {

    @Override
    public boolean check(TokenStream tokenStream) {
        char c = tokenStream.peekChar();
        return c == '\'' || c == '"';
    }

    @Override
    public void process(TokenStream tokenStream) {
        char startCh = tokenStream.peekChar();
        int start = tokenStream.getPos();
        int pos = tokenStream.getPos();
        do {
            pos++;
        } while (pos < tokenStream.getMax() && tokenStream.peekChar(pos) != startCh);

        if (pos == tokenStream.getMax()) {
            throw new ExpressionException("No string terminator, position:" + start);
        }

        tokenStream.addToken(TokenType.LITERAL_STRING, start, pos + 1);
    }
}
