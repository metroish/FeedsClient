package com.midcielab.utility;

import static org.junit.Assert.assertTrue;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class HttpUtilityTest {

    @Parameter(value = 0)
    public String url;
    @Parameter(value = 1)
    public String httpMethod;
    @Parameter(value = 2)
    public String httpBody;

    @Parameters(name = "{index}: with({0},{1},{2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][] { { "https://kaif.io/hot.rss", "GET", "" }, { "https://www.ithome.com.tw/rss", "GET", "" } });
    }

    @Test
    public void testSendRequest() {
        Optional<HttpResponse<String>> resp = HttpUtility.getInstance().sendRequest(url, httpMethod, httpBody);
        resp.ifPresentOrElse(respObj -> {
            assertTrue(respObj.statusCode() > 0);
            assertTrue(respObj.headers().toString().length() > 0);
            assertTrue(respObj.body().toString().length() > 0);
        }, () -> {
            assertTrue(resp.isEmpty());
        });
    }
}
