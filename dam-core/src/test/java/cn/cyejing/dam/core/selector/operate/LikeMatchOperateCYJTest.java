package cn.cyejing.dam.core.selector.operate;

import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.core.selector.match.Match;
import cn.cyejing.dam.core.selector.match.MatchFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LikeMatchOperateCYJTest {

    @Test
    public void test() {
        Match match = MatchFactory.getMatch(EnumMatch.LIKE);
        assertTrue(match.match("foo","weffooqwq"));
        assertFalse(match.match("foo","weff1ooqwq"));
    }


}
