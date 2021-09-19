package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;


public class OrOpTokenReader implements TokenReader {

    @Override
    public boolean check(TokenStream tokenStream) {

        return tokenStream.peekString("||", "或者", "or") != null;
    }

    @Override
    public void process(TokenStream tokenStream) {
        String str = tokenStream.peekString("||", "或者", "or");
        tokenStream.addToken(TokenType.SYMBOLIC_OR, str);
    }
}
