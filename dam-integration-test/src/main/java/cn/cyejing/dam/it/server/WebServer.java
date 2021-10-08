package cn.cyejing.dam.it.server;

import cn.cyejing.dam.common.utils.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebServer {

    private Undertow undertow;

    public WebServer start(int port) {
        this.undertow = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(Handlers.routing()
                        .get("/hello", exchange -> {
                            String header = exchange.getRequestHeaders().get("X-Hello").getFirst();
                            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                            exchange.getResponseHeaders().put(HttpString.tryFromString("X-Hello"), header);
                            exchange.getResponseSender().send(exchange.getQueryParameters().get("hello").getFirst());
                        })
                        .post("/hello",exchange -> {
                            String header = exchange.getRequestHeaders().get("X-Hello").getFirst();
                            FormDataParser parser = FormParserFactory.builder().build().createParser(exchange);
                            parser.parse(e -> {
                                FormData formData = e.getAttachment(FormDataParser.FORM_DATA);
                                e.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                                e.getResponseHeaders().put(HttpString.tryFromString("X-Hello"), header);
                                e.getResponseSender().send(formData.get("hello").getFirst().getValue());
                            });
                        })
                        .post("/hello/json",exchange -> {
                            String header = exchange.getRequestHeaders().get("X-Hello").getFirst();
                            exchange.getRequestReceiver().receiveFullString((e, message) -> {
                                JsonNode jsonNode = JSONUtil.readValue(message);
                                e.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                                e.getResponseHeaders().put(HttpString.tryFromString("X-Hello"), header);
                                e.getResponseSender().send(jsonNode.get("hello").asText());
                            });
                        })
                ).build();
        this.undertow.start();
        log.info("Start Web Server Port:" + port);
        return this;
    }

    public void shutdown() {
        log.info("Shutdown Web Server");
        this.undertow.stop();
    }
}
