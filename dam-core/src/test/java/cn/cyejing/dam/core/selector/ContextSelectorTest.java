package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.config.Condition;
import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.common.enums.EnumType;
import cn.cyejing.dam.core.context.DefaultRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.charset.Charset;

public class ContextSelectorTest {

    @Test(expected = UnsupportedOperationException.class)
    @Ignore
    public void test() {
        DefaultRequest riversRequest = new DefaultRequest(Charset.defaultCharset(), "127.0.0.1",
                "localhost", "/webtest/hello", HttpMethod.GET, HttpHeaderValues.APPLICATION_JSON.toString(),
                null, Mockito.mock(FullHttpRequest.class));
        Condition condition = new Condition();
        condition.setType(EnumType.CONTEXT);
        condition.setMatch(EnumMatch.EQUALS);
        condition.setName("authId");
        condition.setValue("123");
        HttpSelector httpSelector = SelectorFactory.getHttpSelector(condition.getType());
        Assert.assertTrue(httpSelector.test(riversRequest, condition));
    }
}
