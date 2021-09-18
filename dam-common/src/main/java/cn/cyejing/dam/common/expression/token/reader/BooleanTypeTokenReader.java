
package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;


public class BooleanTypeTokenReader implements TokenReader {

    @Override
    public boolean check(TokenStream tokenStream) {
        return tokenStream.peekString("true", "false","真","假") != null;
    }

    @Override
    public void process(TokenStream tokenStream) {
        String str;
        if ((str = tokenStream.peekString("true", "真")) != null) {
            tokenStream.addToken(TokenType.BOOLEAN_TYPE, "true", str);
        } else if ((str = tokenStream.peekString("false", "假")) != null) {
            tokenStream.addToken(TokenType.BOOLEAN_TYPE, "false", str);
        }
    }
}
