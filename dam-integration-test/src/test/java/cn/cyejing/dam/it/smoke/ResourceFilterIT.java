package cn.cyejing.dam.it.smoke;

import cn.cyejing.dam.common.utils.JSONUtil;
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

import static org.junit.Assert.assertEquals;

@Slf4j
public class ResourceFilterIT {

    private DamContainer damContainer;
    private WebServer webServer;


    @Before
    public void setUp() {
        log.info("setUp");
        webServer = new WebServer(4843);
        damContainer = BaseIT.startCore();

        webServer.start();
        damContainer.start();
    }

    @After
    public void shutdown() {
        log.info("shutdown");
        webServer.shutdown();
        damContainer.shutdown();
    }

    @Test
    public void testFile() throws Exception {
        AsyncHttpClient asyncHttpClient = BaseIT.startClient();
        RequestBuilder requestBuilder = new RequestBuilder("GET")
                .setUrl("http://localhost:8048/files/a.json");

        Response response = asyncHttpClient.executeRequest(requestBuilder).get();

        assertEquals(200, response.getStatusCode());
        assertEquals("file", JSONUtil.readValue(response.getResponseBody()).get("hello").asText());
    }
}
