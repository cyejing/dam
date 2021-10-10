package cn.cyejing.dam.it.smoke;

import cn.cyejing.dam.core.container.DamContainer;
import cn.cyejing.dam.it.base.BaseIT;
import cn.cyejing.dam.it.server.WebServer;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

@Slf4j
public class RewriteFilterIT {

    private DamContainer damContainer;
    private WebServer webServer;


    @Before
    public void setUp() {
        log.info("setUp");
        webServer = new WebServer();
        damContainer = BaseIT.startCore();

        webServer.start(4843);
        damContainer.start();
    }

    @After
    public void shutdown() {
        log.info("shutdown");
        webServer.shutdown();
        damContainer.shutdown();
    }

    @Test
    public void testPathRewrite() throws Exception {
        AsyncHttpClient asyncHttpClient = BaseIT.startClient();
        RequestBuilder requestBuilder = new RequestBuilder("GET")
                .setHeader("X-Hello","rewrite")
                .setUrl("http://localhost:8048/replace/hello?hello=rewrite");

        Response response = asyncHttpClient.executeRequest(requestBuilder).get();

        assertEquals(200, response.getStatusCode());
        assertEquals("rewrite", response.getHeader("X-Hello"));
        assertEquals("rewrite", response.getResponseBody());
    }
}
