package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.config.Condition;
import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.common.enums.EnumType;
import cn.cyejing.dam.core.context.DefaultRequest;
import cn.cyejing.dam.core.utils.Utils;
import io.netty.handler.codec.http.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.charset.Charset;

public class CookieSelectorCYJTest {

    @Test
    public void test() {
        DefaultHttpHeaders headers = new DefaultHttpHeaders();
        headers.set(HttpHeaderNames.COOKIE, "foo=foo1;hello=cookie;");
        DefaultRequest riversRequest = new DefaultRequest(Charset.defaultCharset(), "127.0.0.1",
                "localhost", "/webtest/hello", HttpMethod.GET, HttpHeaderValues.APPLICATION_JSON.toString(),
                headers, Mockito.mock(FullHttpRequest.class));
        Condition condition = new Condition();
        condition.setType(EnumType.COOKIE);
        condition.setMatch(EnumMatch.EQUALS);
        condition.setName("hello");
        condition.setValue("cookie");
        HttpSelector httpSelector = SelectorFactory.getHttpSelector(condition.getType());
        Assert.assertTrue(httpSelector.test(riversRequest, condition));
        Assert.assertEquals("cookie", httpSelector.select(Utils.buildExchange(riversRequest), "hello"));
    }
}
