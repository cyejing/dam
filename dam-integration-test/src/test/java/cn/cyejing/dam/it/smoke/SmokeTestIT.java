package cn.cyejing.dam.it.smoke;

import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.container.DamContainer;
import cn.cyejing.dam.core.exception.ErrorCode;
import cn.cyejing.dam.it.base.AssertResponse;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
public class SmokeTestIT {

    private DamContainer damContainer;
    private List<WebServer> webServers = new ArrayList<>();


    @Before
    public void setUp() {
        log.info("setUp");
        for (int port = 4843; port <=4845; port++) {
            webServers.add(new WebServer(port));
        }
        damContainer = BaseIT.startCore();

        webServers.forEach(WebServer::start);
        damContainer.start();
    }

    @After
    public void shutdown() {
        log.info("shutdown");
        webServers.forEach(WebServer::shutdown);
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

    @Test
    public void testNotFoundRoute() throws Exception {
        AsyncHttpClient asyncHttpClient = BaseIT.startClient();
        RequestBuilder requestBuilder = new RequestBuilder("GET")
                .setUrl("http://localhost:8048/notfound/hello");

        Response response = asyncHttpClient.executeRequest(requestBuilder).get();
        AssertResponse.assertEquals(ErrorCode.NOT_FOUND_ROUTE, response);
    }

    @Test
    public void testEmptyFilter() throws Exception {
        AsyncHttpClient asyncHttpClient = BaseIT.startClient();
        RequestBuilder requestBuilder = new RequestBuilder("GET")
                .setUrl("http://localhost:8048/emptyFilter/hello");

        Response response = asyncHttpClient.executeRequest(requestBuilder).get();
        AssertResponse.assertEquals(ErrorCode.FILTER_TAIL, response);
    }
}
