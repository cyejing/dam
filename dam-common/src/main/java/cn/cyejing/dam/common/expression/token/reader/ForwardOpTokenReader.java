
package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;


public class ForwardOpTokenReader implements TokenReader {

    @Override
    public boolean check(TokenStream tokenStream) {
        return tokenStream.peekString("=>", "转发") != null;
    }

    @Override
    public void process(TokenStream tokenStream) {
        String data = tokenStream.peekString("=>", "转发");
        tokenStream.addToken(TokenType.FORWARD, data);
    }
}
