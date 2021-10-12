package cn.cyejing.dam.it.smoke;

import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.container.DamContainer;
import cn.cyejing.dam.it.base.BaseIT;
import cn.cyejing.dam.it.server.WebServer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
public class SmokeTestIT {

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
    public void testGetHello() throws Exception {
        AsyncHttpClient asyncHttpClient = BaseIT.startClient();
        RequestBuilder requestBuilder = new RequestBuilder("GET")
                .setHeader("X-Hello", "hello")
                .setUrl("http://localhost:8048/replace/hello?hello=testGetHello");

        Response response = asyncHttpClient.executeRequest(requestBuilder).get();

        assertEquals(200, response.getStatusCode());
        assertEquals("hello", response.getHeader("X-Hello"));
        assertEquals("testGetHello", response.getResponseBody());
    }


    @Test
    public void testPostHello() throws Exception {
        AsyncHttpClient asyncHttpClient = BaseIT.startClient();
        RequestBuilder requestBuilder = new RequestBuilder("POST")
                .setHeader("X-Hello", "hello")
                .setUrl("http://localhost:8048/replace/hello?hello=hello")
                .addFormParam("hello", "testPostHello");


        Response response = asyncHttpClient.executeRequest(requestBuilder).get();

        assertEquals(200, response.getStatusCode());
        assertEquals("hello", response.getHeader("X-Hello"));
        assertEquals("testPostHello", response.getResponseBody());
    }

    @Test
    public void testPostJSONHello() throws Exception {
        AsyncHttpClient asyncHttpClient = BaseIT.startClient();
        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put("hello", "testPostJSONHello");
        RequestBuilder requestBuilder = new RequestBuilder("POST")
                .setHeader("X-Hello", "hello")
                .setUrl("http://localhost:8048/replace/hello/json?hello=hello")
                .setBody(objectNode.toString());


        Response response = asyncHttpClient.executeRequest(requestBuilder).get();

        assertEquals(200, response.getStatusCode());
        assertEquals("hello", response.getHeader("X-Hello"));
        assertEquals("testPostJSONHello", response.getResponseBody());
    }
}
