
package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;


public class UselessCharReader implements TokenReader {
    @Override
    public boolean check(TokenStream tokenStream) {
        char c = tokenStream.peekChar();
        return c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\0';
    }

    @Override
    public void process(TokenStream tokenStream) {
        tokenStream.skipPos();
    }
}
