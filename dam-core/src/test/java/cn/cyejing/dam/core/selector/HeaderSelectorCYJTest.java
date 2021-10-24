package cn.cyejing.dam.core.selector;

import cn.cyejing.dam.common.config.Condition;
import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.common.enums.EnumType;
import cn.cyejing.dam.core.context.DefaultRequest;
import cn.cyejing.dam.core.utils.Utils;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.charset.Charset;

public class HeaderSelectorCYJTest {

    @Test
    public void test() {
        DefaultHttpHeaders headers = new DefaultHttpHeaders();
        headers.set("X-Test","hello");
        DefaultRequest request = new DefaultRequest(Charset.defaultCharset(), "127.0.0.1",
                "localhost", "/webtest/hello", HttpMethod.GET, HttpHeaderValues.APPLICATION_JSON.toString(),
                headers, Mockito.mock(FullHttpRequest.class));
        Condition condition = new Condition();
        condition.setType(EnumType.HEADER);
        condition.setMatch(EnumMatch.EQUALS);
        condition.setName("X-Test");
        condition.setValue("hello");
        HttpSelector httpSelector = SelectorFactory.getHttpSelector(condition.getType());
        Assert.assertTrue(httpSelector.test(request, condition));
        Assert.assertEquals("hello", httpSelector.select(Utils.buildExchange(request), "X-Test"));
    }

}
