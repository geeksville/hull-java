package io.hull.util;


import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.Cookie;
import java.util.Map;

public class HullUtilsTest {
    @Test
    public void testGetCookieValue() throws Exception {
        Cookie[] cookies = new Cookie[3];
        cookies[0] = new Cookie("format","json");
        cookies[1] = new Cookie("hull_12345","abcdefg");
        cookies[2] = new Cookie("key","val");

        Assert.assertEquals(HullUtils.getCookieValue(cookies, "format"), "json");
        Assert.assertEquals(HullUtils.getCookieValue(cookies, "hull_12345"), "abcdefg");
        Assert.assertEquals(HullUtils.getCookieValue(cookies, "key"), "val");
        Assert.assertNull(HullUtils.getCookieValue(cookies, "unknown"));
    }

    @Test
    public void testToMap() throws Exception {
        String json = "{\"id\":\"51e9665f6a4e69d83500001a\",\"updated_at\":\"2013-07-19T16:16:31Z\"}";
        Map<String, Object> map = HullUtils.toMap(json);
        Assert.assertEquals(map.get("id"), "51e9665f6a4e69d83500001a");
        Assert.assertEquals(map.get("updated_at"), "2013-07-19T16:16:31Z");
    }
}
