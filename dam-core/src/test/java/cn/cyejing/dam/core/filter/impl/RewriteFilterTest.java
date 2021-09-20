package cn.cyejing.dam.core.filter.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class RewriteFilterTest {

    @Test
    public void testReplacePath() {
        RewriteFilter rewriteFilter = new RewriteFilter();
        RewriteFilter.Config config = new RewriteFilter.Config();
        config.setRegex("^/foo/(.*)");
        config.setReplacement("/loo/$1");
        String newPath = rewriteFilter.replacePath("/foo/boo", config);
        assertEquals("/loo/boo",newPath);
    }

    @Test
    public void testReplacePath1() {
        RewriteFilter rewriteFilter = new RewriteFilter();
        RewriteFilter.Config config = new RewriteFilter.Config();
        config.setRegex("/consumingServiceEndpoint");
        config.setReplacement("/backingServiceEndpoint");
        String newPath = rewriteFilter.replacePath("/consumingServiceEndpoint/foo", config);
        assertEquals("/backingServiceEndpoint/foo",newPath);
    }

    @Test
    public void testReplacePath2() {
        RewriteFilter rewriteFilter = new RewriteFilter();
        RewriteFilter.Config config = new RewriteFilter.Config();
        config.setRegex("/a/b/(?<segment>.*)");
        config.setReplacement("/f/${segment}");
        String newPath = rewriteFilter.replacePath("/a/b/foo", config);
        assertEquals("/f/foo",newPath);
    }

}
