package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;


public class AndOpTokenReader implements TokenReader {

    @Override
    public boolean check(TokenStream tokenStream) {
        return tokenStream.peekString("&&", "并且", "and", "AND", "And") != null;
    }

    @Override
    public void process(TokenStream tokenStream) {
        String str = tokenStream.peekString("&&", "并且", "and", "AND", "And");
        tokenStream.addToken(TokenType.SYMBOLIC_AND, str);
    }
}
