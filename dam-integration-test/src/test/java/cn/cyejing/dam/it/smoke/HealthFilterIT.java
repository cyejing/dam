package cn.cyejing.dam.it.smoke;

import cn.cyejing.dam.core.container.DamContainer;
import cn.cyejing.dam.it.base.BaseIT;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HealthFilterIT {

    private DamContainer damContainer;

    @Before
    public void setUp() {
        damContainer = BaseIT.startCore();
        damContainer.start();
    }
    @After
    public void tearDown() {
        damContainer.shutdown();
    }

    @Test
    public void healthTest() throws Exception {
        AsyncHttpClient asyncHttpClient = BaseIT.startClient();
        RequestBuilder requestBuilder = new RequestBuilder("GET")
                .setUrl("http://localhost:8048/health");

        Response response = asyncHttpClient.executeRequest(requestBuilder).get();

        assertEquals(200, response.getStatusCode());
        assertEquals("up", response.getResponseBody());
    }
}
