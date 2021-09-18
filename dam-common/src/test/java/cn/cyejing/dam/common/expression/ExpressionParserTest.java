package cn.cyejing.dam.common.expression;

import cn.cyejing.dam.common.enums.EnumOperator;
import cn.cyejing.dam.common.module.Condition;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class ExpressionParserTest {

    @Test
    public void test1() {
        Expression expression = ExpressionParser.parse("真 并且 假 或者 (真 并且 假)");
        assertFalse(expression.evaluateBoolean(null));
        System.out.println(expression.evaluateBoolean(null));

    }

    @Test
    public void test2() throws ExpressionException {
        Expression expression = ExpressionParser.parse("true && false || false &&(false||true)");
        assertFalse(expression.evaluateBoolean(null));
        System.out.println(expression.toStringAST());
        Expression e1 = ExpressionParser.parse("((true and false) or (false and (false or true)))");
        assertFalse(e1.evaluateBoolean(null));
    }


    @Test
    public void testForward() {
        String expr = "false || true && false => '192.168.1.1'; \n" +
                "false || true && false => '192.168.1.2'; \n" +
                "false || true && false => '192.168.1.3'    ;" +
                "true || ((true||false) && false) => '192.168.1.4'; " +
                "false || true && false => '192.168.1.3'; ";

        Expression expression = ExpressionParser.parse(expr);
        System.out.println(expression.toStringAST());
        assertEquals("192.168.1.4", expression.evaluateString(null));
    }

    @Test(expected = ExpressionException.class)
    public void testError21() {
        try {
            String expr = "Path.antMatch('') && Header['asd']('')";
            Expression expression = ExpressionParser.parse(expr);
            System.out.println(expression.toStringAST());
        } catch (Exception e) {
            System.out.println(e);
            assertTrue(e.getMessage().contains("Grammatical errors, position(35):'...& Header['asd'](...'"));
            throw e;
        }
    }

    @Test(expected = ExpressionException.class)
    public void testError() {
        String expr = "(true||false) && (false&&true)||)false";
        Expression expression = ExpressionParser.parse(expr);
        System.out.println(expression.toStringAST());
    }

    @Test(expected = ExpressionException.class)
    public void testError1() {
        try {
            String expr = "(true||(false)) && (false))&&true && false";
            Expression expression = ExpressionParser.parse(expr);
            System.out.println(expression.toStringAST());
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Grammatical errors, position(27):'...se)) && (false))...'"));
            throw e;
        }
    }

    @Test(expected = ExpressionException.class)
    public void testError2() {
        try {
            String expr = "true && false PATH || false";
            Expression expression = ExpressionParser.parse(expr);
            System.out.println(expression.toStringAST());
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Grammatical errors, position(18):'...true && false PATH...'"));
            throw e;
        }
    }

}
