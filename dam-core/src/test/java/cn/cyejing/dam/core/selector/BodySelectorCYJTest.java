package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.config.Condition;
import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.common.enums.EnumType;
import cn.cyejing.dam.core.context.DefaultRequest;
import cn.cyejing.dam.core.utils.Utils;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BodySelectorCYJTest {


    @Test
    public void testFromData() {
        DefaultHttpHeaders headers = new DefaultHttpHeaders();
        FullHttpRequest fullHttpRequest = Mockito.mock(FullHttpRequest.class);
        Mockito.when(fullHttpRequest.content()).thenReturn(Unpooled.copiedBuffer("foo=123&hello=xsyx", StandardCharsets.UTF_8));
        DefaultRequest riversRequest = new DefaultRequest(Charset.defaultCharset(), "127.0.0.1",
                "www.xsyx.com", "/webtest/hello", HttpMethod.POST, HttpHeaderValues.FORM_DATA.toString(),
                headers, fullHttpRequest);
        Condition condition = new Condition();
        condition.setType(EnumType.BODY);
        condition.setMatch(EnumMatch.EQUALS);
        condition.setName("hello");
        condition.setValue("xsyx");
        HttpSelector httpSelector = SelectorFactory.getHttpSelector(condition.getType());
        Assert.assertTrue(httpSelector.test(riversRequest, condition));
        Assert.assertEquals("xsyx", httpSelector.select(Utils.buildExchange(riversRequest), "hello"));

    }

}
