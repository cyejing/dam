package cn.cyejing.dam.core.selector.operate;

import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.core.selector.match.Match;
import cn.cyejing.dam.core.selector.match.MatchFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IPMatchOperateCYJTest {

    @Test
    public void match() {
        Match ipMatchOperate = MatchFactory.getMatch(EnumMatch.IP);
        assertTrue(ipMatchOperate.match("192.168.2.1", "192.168.2.1"));
        assertTrue(ipMatchOperate.match(" 192.168.2.1 ", "192.168.2.1"));
        assertTrue(ipMatchOperate.match("192.168.1.0/24", "192.168.1.1:123"));
        assertTrue(ipMatchOperate.match("192.168.1.0/24", "192.168.1.1"));
        assertTrue(ipMatchOperate.match("192.168.1.0/24", "192.168.1.254"));
        assertTrue(ipMatchOperate.match("192.168.1.0/24", "192.168.1.255"));
        assertFalse(ipMatchOperate.match("192.168.1.0/24", "192.168.2.1"));
        assertFalse(ipMatchOperate.match("192.168.1.0/23", "192.168.2.1"));
        assertFalse(ipMatchOperate.match("192.168.1.0/23", " 192.168.2.1 "));
    }

    @Test
    public void matchError1() {
        Match ipMatchOperate = MatchFactory.getMatch(EnumMatch.IP);
        assertFalse(ipMatchOperate.match("192.168.1.0/-1", "192.168.1.1"));
    }

    @Test(expected = RuntimeException.class)
    public void matchError2() {
        Match ipMatchOperate = MatchFactory.getMatch(EnumMatch.IP);
        assertFalse(ipMatchOperate.match("wefw", "192wefw"));
    }
}
