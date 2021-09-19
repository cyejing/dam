package cn.cyejing.dam.common.expression.token.reader;

import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.common.expression.token.TokenReader;
import cn.cyejing.dam.common.expression.token.TokenStream;
import cn.cyejing.dam.common.expression.token.TokenType;

import java.util.*;


public class ConditionOperateTokenReader implements TokenReader {

    private static final String[] ALL;
    private static final String[] OP_EQUALS = new String[]{"equals", "Equals", "等于", "anyMatch", "AnyMatch", "任意匹配"};
    private static final String[] OP_ANT_MATCH = new String[]{"AntMatch", "antMatch", "路径匹配"};
    private static final String[] OP_LIKE_MATCH = new String[]{"likeMatch", "LikeMatch", "模糊匹配", "包含", "contains", "Contains"};
    private static final String[] OP_IP_MATCH = new String[]{"ipMatch", "IpMatch", "Ip匹配", "地址匹配"};
    private static final String[] OP_REGEX_MATCH = new String[]{"regexMatch", "RegexMatch", "正则匹配"};

    private static final Map<String, String[]> MAP = new HashMap<>();


    static {
        List<String> allType = new ArrayList<>();
        allType.addAll(Arrays.asList(OP_EQUALS));
        allType.addAll(Arrays.asList(OP_ANT_MATCH));
        allType.addAll(Arrays.asList(OP_LIKE_MATCH));
        allType.addAll(Arrays.asList(OP_IP_MATCH));
        allType.addAll(Arrays.asList(OP_REGEX_MATCH));
        ALL = allType.toArray(new String[0]);

        MAP.put(EnumMatch.EQUALS.name(), OP_EQUALS);
        MAP.put(EnumMatch.ANT.name(), OP_ANT_MATCH);
        MAP.put(EnumMatch.LIKE.name(), OP_LIKE_MATCH);
        MAP.put(EnumMatch.IP.name(), OP_IP_MATCH);
        MAP.put(EnumMatch.REGEX.name(), OP_REGEX_MATCH);
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

    private boolean maybeToken(TokenStream tokenStream, String[] strings, String key) {
        String type;
        if ((type = tokenStream.peekString(strings)) != null) {
            tokenStream.addToken(TokenType.CONDITION_OPERATE, key, type);
            return true;
        }
        return false;
    }
}
