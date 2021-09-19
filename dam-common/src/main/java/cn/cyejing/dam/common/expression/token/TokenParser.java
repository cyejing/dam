package cn.cyejing.dam.common.expression.token;

import cn.cyejing.dam.common.expression.ExpressionException;
import cn.cyejing.dam.common.expression.token.reader.*;

import java.util.ArrayList;
import java.util.List;


public class TokenParser {

    private final TokenStream tokenStream;
    private final List<TokenReader> tokenReaders;

    public TokenParser(String expressionStr) {
        this.tokenStream = new TokenStream(expressionStr);
        this.tokenReaders = new ArrayList<>();

        this.tokenReaders.add(new UselessCharReader());
        this.tokenReaders.add(new SymbolTokenReader());
        this.tokenReaders.add(new NotOpTokenReader());
        this.tokenReaders.add(new AndOpTokenReader());
        this.tokenReaders.add(new OrOpTokenReader());
        this.tokenReaders.add(new ForwardOpTokenReader());
        this.tokenReaders.add(new LiteralStringTokenReader());
        this.tokenReaders.add(new BooleanTypeTokenReader());
        this.tokenReaders.add(new ConditionOperateTokenReader());
        this.tokenReaders.add(new ConditionTypeTokenReader());

    }

    public List<Token> process() {
        while (!tokenStream.isEnd()) {
            int pos = tokenStream.getPos();
            for (TokenReader reader : tokenReaders) {
                if (reader.check(tokenStream)) {
                    reader.process(tokenStream);
                    if (tokenStream.isEnd()) {
                        break;
                    }
                }
            }
            if (pos == tokenStream.getPos()) {
                char ch = tokenStream.peekChar();
                throw new ExpressionException("Can't process the character (" + (int) ch + ") '" + ch + "'" + " position:" + pos);
            }
        }
        return tokenStream.getTokens();
    }
}
