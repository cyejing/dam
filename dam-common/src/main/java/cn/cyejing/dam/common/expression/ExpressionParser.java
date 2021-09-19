
package cn.cyejing.dam.common.expression;

import cn.cyejing.dam.common.expression.ast.ASTParser;
import cn.cyejing.dam.common.expression.ast.Node;
import cn.cyejing.dam.common.expression.token.Token;
import cn.cyejing.dam.common.expression.token.TokenParser;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class ExpressionParser {

    public static Expression parse(String expressionStr) throws ExpressionException {
        if (StringUtils.isEmpty(expressionStr)) {
            throw new ExpressionException("Expressions cannot be null");
        }
        List<Token> tokens = new TokenParser(expressionStr).process();
        Node ast = new ASTParser(expressionStr,tokens).process();
        return new DefaultExpressionImpl(expressionStr, ast);
    }

}
