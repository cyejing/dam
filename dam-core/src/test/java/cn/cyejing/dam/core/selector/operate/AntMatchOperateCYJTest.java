package cn.cyejing.dam.core.selector.operate;

import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.core.selector.match.Match;
import cn.cyejing.dam.core.selector.match.MatchFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AntMatchOperateCYJTest {

    @Test
    public void match() {
        Match matchOperate = MatchFactory.getMatch(EnumMatch.ANT);
        assertTrue(matchOperate.match("/app/*.x","/app/asdqw.x"));
        assertFalse(matchOperate.match("/app/*.x","/app/qwdqw/asdqw.x"));

        assertTrue(matchOperate.match("/app/**/*.x","/app/as/qwddqw.x"));

        assertTrue(matchOperate.match("/app/**","/app/wdqwq/fwew/ef/"));
        assertFalse(matchOperate.match("/app/**","/appqw/wew"));
        assertTrue(matchOperate.match("/app/**","/app"));
        assertTrue(matchOperate.match("/app/**","/app/"));

        assertTrue(matchOperate.match("/Store**/**","/StoreHome/asd"));
        assertTrue(matchOperate.match("/Store**/**","/StoreCon/asd"));

    }
}
