package cn.cyejing.dam.common.expression.token;

import cn.cyejing.dam.common.expression.ExpressionException;
import cn.cyejing.dam.common.expression.ExpressionParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class TokenParserTest {

    @Test
    public void testToken() {
        List<Token> process = new TokenParser("PATH.anyMatch('/dam/**') || Host.anyMatch('http://localhost') || Header['X-Tags'].equals('tag')").process();
        assertEquals("/dam/**", process.get(4).getData());
        assertEquals(TokenType.SYMBOLIC_OR, process.get(6).getType());
    }

    @Test
    public void test1() {
        String expr = "(true||(true||(((false||(true&& ((false||true)&&true)))&&false)||true))) && ((false&&true)||false)";
        List<Token> process = new TokenParser(expr).process();
        Assert.assertEquals(ExpressionParser.parse(expr).evaluateBoolean(null), false);
    }

    @Test(expected = ExpressionException.class)
    public void testError() {
        try {
            new TokenParser("(true||false) && (*false&&true)||false)").process();
        } catch (Exception e) {
            assertEquals("Can't process the character (42) '*' position:18", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testForward() {
        List<Token> process = new TokenParser("ClientIp.AnyMatch('127.0.0.1') => '192.168.1.1'").process();
        assertEquals(process.get(0).getData(), "ClientIp");
        assertEquals(process.get(0).getType(), TokenType.CONDITION_TYPE);
    }

}
