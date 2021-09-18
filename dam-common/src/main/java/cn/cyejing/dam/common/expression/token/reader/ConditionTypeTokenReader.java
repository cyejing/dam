
package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.enums.EnumType;
import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;

import java.util.*;


public class ConditionTypeTokenReader implements TokenReader {

    private static final String[] ALL;
    private static final String[] TYPE_PATH = new String[]{
            "PATH", "Path", "path", "路径"
    };
    private static final String[] TYPE_HEADER = new String[]{
            "HEADER", "Header", "header", "请求头", "头"
    };
    private static final String[] TYPE_HOST = new String[]{
            "HOST", "Host", "host", "域名"
    };
    private static final String[] TYPE_METHOD = new String[]{
            "METHOD", "Method", "method", "方法"
    };
    private static final String[] TYPE_IP = new String[]{
            "ClientIP", "ClientIp", "客户端地址", "客户端Ip", "客户端IP"
    };
    private static final String[] TYPE_COOKIE = new String[]{
            "COOKIE", "Cookie", "cookie", "会话"
    };
    private static final String[] TYPE_QUERY = new String[]{
            "QUERY", "Query", "query", "参数"
    };
    private static final String[] TYPE_BODY = new String[]{
            "BODY", "Body", "body", "FORM", "Form", "form", "表单"
    };
    private static final String[] TYPE_CONTEXT = new String[]{
            "CONTEXT", "Context", "context", "上下文"
    };


    private static final Map<String, String[]> MAP = new HashMap<>();
    private static final Map<String, TokenType> MAP_TOKEN_TYPE = new HashMap<>();

    static {
        List<String> allType = new ArrayList<>();
        allType.addAll(Arrays.asList(TYPE_PATH));
        allType.addAll(Arrays.asList(TYPE_HEADER));
        allType.addAll(Arrays.asList(TYPE_HOST));
        allType.addAll(Arrays.asList(TYPE_METHOD));
        allType.addAll(Arrays.asList(TYPE_IP));
        allType.addAll(Arrays.asList(TYPE_COOKIE));
        allType.addAll(Arrays.asList(TYPE_QUERY));
        allType.addAll(Arrays.asList(TYPE_BODY));
        allType.addAll(Arrays.asList(TYPE_CONTEXT));
        ALL = allType.toArray(new String[0]);

        MAP.put(EnumType.PATH.name(), TYPE_PATH);
        MAP.put(EnumType.HEADER.name(), TYPE_HEADER);
        MAP.put(EnumType.HOST.name(), TYPE_HOST);
        MAP.put(EnumType.METHOD.name(), TYPE_METHOD);
        MAP.put(EnumType.IP.name(), TYPE_IP);
        MAP.put(EnumType.COOKIE.name(), TYPE_COOKIE);
        MAP.put(EnumType.QUERY.name(), TYPE_QUERY);
        MAP.put(EnumType.BODY.name(), TYPE_BODY);
        MAP.put(EnumType.CONTEXT.name(), TYPE_CONTEXT);


        MAP_TOKEN_TYPE.put(EnumType.PATH.name(), TokenType.CONDITION_TYPE);
        MAP_TOKEN_TYPE.put(EnumType.HEADER.name(), TokenType.CONDITION_TYPE_NAME);
        MAP_TOKEN_TYPE.put(EnumType.HOST.name(), TokenType.CONDITION_TYPE);
        MAP_TOKEN_TYPE.put(EnumType.METHOD.name(), TokenType.CONDITION_TYPE);
        MAP_TOKEN_TYPE.put(EnumType.IP.name(), TokenType.CONDITION_TYPE);
        MAP_TOKEN_TYPE.put(EnumType.COOKIE.name(), TokenType.CONDITION_TYPE_NAME);
        MAP_TOKEN_TYPE.put(EnumType.QUERY.name(), TokenType.CONDITION_TYPE_NAME);
        MAP_TOKEN_TYPE.put(EnumType.BODY.name(), TokenType.CONDITION_TYPE_NAME);
        MAP_TOKEN_TYPE.put(EnumType.CONTEXT.name(), TokenType.CONDITION_TYPE_NAME);

    }


    @Override
    public boolean check(TokenStream tokenStream) {
        return tokenStream.peekString(ALL) != null;
    }

    @Override
    public void process(TokenStream tokenStream) {
        for (Map.Entry<String, String[]> entry : MAP.entrySet()) {
            if (maybeToken(tokenStream, entry.getValue(), entry.getKey())) {
                return;
            }
        }
    }


    private boolean maybeToken(TokenStream tokenStream,String[] strings, String key) {
        String type;
        if ((type = tokenStream.peekString(strings)) != null) {
            tokenStream.addToken(MAP_TOKEN_TYPE.get(key), key, type);
            return true;
        }
        return false;
    }
}
