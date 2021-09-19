package cn.cyejing.dam.common.expression.ast;

import cn.cyejing.dam.common.expression.ExpressionException;
import cn.cyejing.dam.common.expression.token.Token;
import cn.cyejing.dam.common.expression.token.TokenType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class ASTParser {

    private final Deque<Node> nodeStack = new ArrayDeque<>();

    private final String expressionStr;
    private final List<Token> tokenStream;
    private int pos;

    public ASTParser(String expressionStr, List<Token> tokenStream) {
        this.expressionStr = expressionStr;
        this.tokenStream = tokenStream;
        this.pos = 0;
    }


    public Node process() {
        Node node = parseStr();
        if (node == null) {
            throw new ExpressionException("Did not resolve to expression");
        }
        Token t = peekToken();
        if (t != null) {
            throw newParseException(t, "Unrecognized syntax");
        }
        return node;
    }

    private Node parseStr() {
        Node node = parseOrOp();
        if (peekToken(TokenType.FORWARD)) {
            List<Node> nodes = new ArrayList<>();
            do {
                nextToken();
                Token token = takeToken(TokenType.LITERAL_STRING);
                checkNode(token, node);
                nodes.add(new OpForwardNode(token.getData(), node));
                takeToken(TokenType.SEMICOLON);
                node = parseOrOp();
            } while (peekToken(TokenType.FORWARD));
            node = new CombineNode(nodes.toArray(new Node[0]));
        }

        return node;
    }

    private Node parseOrOp() {
        Node node = parseAndOp();
        while (peekToken(TokenType.SYMBOLIC_OR)) {
            Token t = takeToken();
            Node rN = parseAndOp();
            checkNode(t, node, rN);
            node = new OpOrNode(node, rN);
        }
        return node;
    }

    private Node parseAndOp() {
        Node node = parseNotOP();
        while (peekToken(TokenType.SYMBOLIC_AND)) {
            Token t = takeToken();
            Node rN = parseNotOP();
            checkNode(t, node, rN);
            node = new OpAndNode(node, rN);
        }
        return node;
    }

    private Node parseNotOP() {
        if (peekToken(TokenType.NOT)) {
            takeToken();
            Node node = parsePrimary();
            return new NotNode(node);
        }
        return parsePrimary();
    }

    private Node parsePrimary() {
        if (maybeCondition()) {
            return pop();
        } else if (maybeBoolean()) {
            return pop();
        } else if (maybeParen()) {
            return pop();
        }
        return null;
    }

    private boolean maybeParen() {
        if (peekToken(TokenType.LPAREN)) {
            Token token = takeToken();
            Node node = parseOrOp();
            checkNode(token, node);
            Token nN = nextToken();
            if (nN == null || nN.getType() != TokenType.RPAREN) {
                throw newParseException(nN, "No correct right bracket terminator");
            }
            push(node);
            return true;
        }
        return false;
    }


    private boolean maybeBoolean() {
        if (peekToken(TokenType.BOOLEAN_TYPE)) {
            Token token = takeToken();
            push(new BooleanNode(token.getKey()));
            return true;
        }
        return false;
    }

    private boolean maybeCondition() {
        if (peekToken(TokenType.CONDITION_TYPE)) {
            Token typeToken = takeToken(TokenType.CONDITION_TYPE);
            takeToken(TokenType.DOT);
            Token opToken = takeToken(TokenType.CONDITION_OPERATE);
            takeToken(TokenType.LPAREN);
            Token strToken = takeToken(TokenType.LITERAL_STRING);
            takeToken(TokenType.RPAREN);
            push(new ConditionNode(typeToken.getKey(), null, opToken.getKey(), strToken.getData()));
            return true;
        } else if (peekToken(TokenType.CONDITION_TYPE_NAME)) {
            Token typeToken = takeToken(TokenType.CONDITION_TYPE_NAME);
            takeToken(TokenType.LSQUARE);
            Token nameToken = takeToken(TokenType.LITERAL_STRING);
            takeToken(TokenType.RSQUARE);
            takeToken(TokenType.DOT);
            Token opToken = takeToken(TokenType.CONDITION_OPERATE);
            takeToken(TokenType.LPAREN);
            Token valueToken = takeToken(TokenType.LITERAL_STRING);
            takeToken(TokenType.RPAREN);
            push(new ConditionNode(typeToken.getKey(), nameToken.getData(), opToken.getKey(), valueToken.getData()));
            return true;
        }
        return false;
    }

    private void checkNode(Token t, Node lN, Node rN) {
        if (lN == null) {
            throw newParseException(t, "");
        }
        if (rN == null) {
            throw newParseException(t, "");
        }
    }

    private void checkNode(Token t, Node node) {
        if (node == null) {
            throw newParseException(t, "");
        }
    }

    private Token peekToken() {
        if (this.pos >= this.tokenStream.size()) {
            return null;
        }
        return tokenStream.get(pos);
    }

    private boolean peekToken(TokenType type) {
        Token token = peekToken();
        return token != null && token.getType() == type;
    }

    private Token takeToken(TokenType type) {
        if (this.pos >= this.tokenStream.size()) {
            throw new ExpressionException("No more characters" + type.name() + ":" + type.getTokenStr());
        }
        Token token = takeToken();
        if (token.getType() != type) {
            throw newParseException(token, "Expected type error" + type.name() + ":" + type.getTokenStr());
        }
        return token;
    }

    private Token takeToken() {
        if (this.pos >= this.tokenStream.size()) {
            throw new ExpressionException("No more characters");
        }
        return tokenStream.get(pos++);
    }

    private Token nextToken() {
        if (this.pos >= this.tokenStream.size()) {
            return null;
        }
        return tokenStream.get(pos++);
    }

    private void push(Node newNode) {
        this.nodeStack.push(newNode);
    }

    private Node pop() {
        return this.nodeStack.pop();
    }

    private ExpressionException newParseException(Token t, String msg) {
        if (t == null) {
            return new ExpressionException(msg);
        }
        int start = Math.max(t.getStartPos() - 15, 0);
        int end = t.getEndPos();
        StringBuilder sb = new StringBuilder("Grammatical errors, position(" + end + "):'...");
        sb.append(this.expressionStr, start, end);
        sb.append("...'");
        if (StringUtils.isNotEmpty(msg)) {
            sb.append(" description:").append(msg);
        }
        return new ExpressionException(sb.toString());
    }
}
