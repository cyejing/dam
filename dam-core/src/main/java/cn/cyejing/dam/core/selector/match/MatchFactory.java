package cn.cyejing.dam.core.selector.match;


import cn.cyejing.dam.common.enums.EnumMatch;

import java.util.HashMap;
import java.util.Map;


public class MatchFactory {

    private static final cn.cyejing.dam.core.selector.match.MatchFactory INSTANCE = new cn.cyejing.dam.core.selector.match.MatchFactory();
    private final Map<EnumMatch, Match> matchOperatorMap = new HashMap<>();

    private MatchFactory() {
        matchOperatorMap.put(EnumMatch.EQUALS, new AnyMatch());
        matchOperatorMap.put(EnumMatch.ANT, new AntMatch());
        matchOperatorMap.put(EnumMatch.LIKE, new LikeMatch());
        matchOperatorMap.put(EnumMatch.IP, new IPMatch());
        matchOperatorMap.put(EnumMatch.REGEX, new RegexMatch());
    }


    public static Match getMatch(EnumMatch match) {
        return INSTANCE.matchOperatorMap.get(match);
    }
}
