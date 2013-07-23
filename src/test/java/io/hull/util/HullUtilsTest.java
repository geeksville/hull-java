package io.hull.util;


import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.Cookie;
import java.util.Map;

public class HullUtilsTest {
    private static String APP_SECRET = "707fdf84fd8c15153f9a52fc85c8e927";
    private static String HULL_USER_ID = "51a62937d120117a64000013";
    private static String HULL_USER_SIG = "1374469098.1ccf1506eef6a471772887f8e68576bd946cb842";

    @Test
    public void testGetCookieValue() throws Exception {
        Cookie[] cookies = new Cookie[4];
        cookies[0] = new Cookie("format","json");
        cookies[1] = new Cookie("hull_12345","abcdefg");
        cookies[2] = new Cookie("key","val");
        cookies[3] = new Cookie("hash","eyJuYW1lIjoiQ3J5c3RhbCBDaGFuZyIsImlkIjoiY3J5c3Rh%3D%3D");

        Assert.assertEquals(HullUtils.getURLDecodedCookieValue(cookies, "format"), "json");
        Assert.assertEquals(HullUtils.getURLDecodedCookieValue(cookies, "hull_12345"), "abcdefg");
        Assert.assertEquals(HullUtils.getURLDecodedCookieValue(cookies, "key"), "val");
        Assert.assertEquals(HullUtils.getURLDecodedCookieValue(cookies, "hash"), "eyJuYW1lIjoiQ3J5c3RhbCBDaGFuZyIsImlkIjoiY3J5c3Rh==");
        Assert.assertNull(HullUtils.getURLDecodedCookieValue(cookies, "unknown"));
    }

    @Test
    public void testToMap() throws Exception {
        String json = "{\"id\":\"51e9665f6a4e69d83500001a\",\"updated_at\":\"2013-07-19T16:16:31Z\"}";
        Map<String, Object> map = HullUtils.toMap(json);
        Assert.assertEquals(map.get("id"), "51e9665f6a4e69d83500001a");
        Assert.assertEquals(map.get("updated_at"), "2013-07-19T16:16:31Z");
    }

    @Test
    public void testReadCookie() throws Exception {
        String cookieVal = "eyJIdWxsLVVzZXItSWQiOiI1MWE2MjkzN2QxMjAxMTdhNjQwMDAwMTMiLCJIdWxsLVVzZXItU2lnIjoiMTM3NDQ2OTA5OC4xY2NmMTUwNmVlZjZhNDcxNzcyODg3ZjhlNjg1NzZiZDk0NmNiODQyIn0%3D";
        Map<String, String> result = HullUtils.decodeCookie(cookieVal);
        Assert.assertEquals(result.get("Hull-User-Id"), HULL_USER_ID);
        Assert.assertEquals(result.get("Hull-User-Sig"), HULL_USER_SIG);
    }

    @Test
    public void testCurrentUserId() throws Exception {
        String userId = HullUtils.currentUserId(HULL_USER_ID, HULL_USER_SIG, APP_SECRET);
        Assert.assertEquals(userId, HULL_USER_ID);
    }
}
