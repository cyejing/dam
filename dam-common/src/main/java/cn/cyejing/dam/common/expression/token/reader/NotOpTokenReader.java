package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;


public class NotOpTokenReader implements TokenReader {

    @Override
    public boolean check(TokenStream tokenStream) {
        return tokenStream.peekString("!", "非", "not", "Not", "NOT") != null;
    }

    @Override
    public void process(TokenStream tokenStream) {
        String str = tokenStream.peekString("!", "非", "not", "Not", "NOT");
        tokenStream.addToken(TokenType.NOT, str);
    }
}
