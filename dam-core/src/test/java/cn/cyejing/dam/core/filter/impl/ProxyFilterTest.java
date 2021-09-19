package cn.cyejing.dam.core.filter.impl;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

public class ProxyFilterTest {

    @Test
    public void testUrl() throws MalformedURLException, URISyntaxException {
        URL url = new URL("http://baidu.com:123/foo");
        assertEquals("http", url.getProtocol());
        assertEquals("baidu.com", url.getHost());
        assertEquals(123, url.getPort());

        URI url2 = new URI("lb://host:123/foo");
        assertEquals("lb", url2.getScheme());
        assertEquals("host", url2.getHost());
        assertEquals(123, url.getPort());


    }

}
