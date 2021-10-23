package cn.cyejing.dam.core.selector.operate;

import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.core.selector.match.Match;
import cn.cyejing.dam.core.selector.match.MatchFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualsMatchOperateCYJTest {

    @Test
    public void test() {
        Match match = MatchFactory.getMatch(EnumMatch.EQUALS);
        assertTrue(match.match("foo","foo"));
        assertFalse(match.match("foo1","foo"));
        assertTrue(match.match("foo,boo","foo"));
        assertTrue(match.match("foo,boo","boo"));
    }

}
