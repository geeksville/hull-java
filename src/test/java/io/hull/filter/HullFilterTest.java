package io.hull.filter;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: crystal
 * Date: 7/21/13
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class HullFilterTest {
    private static HullFilter filter;
    private static String APP_ID = "51a5541ed12011552e000005";
    private static String APP_SECRET = "707fdf84fd8c15153f9a52fc85c8e927";
    private static String ORG_URL = "https://123abcd.hullapp.io";
    private static String HULL_USER_ID = "51a62937d120117a64000013";
    private static String HULL_USER_SIG = "1374469098.1ccf1506eef6a471772887f8e68576bd946cb842";

    @BeforeClass
    public static void setUp() {
        filter = new HullFilter(APP_ID, APP_SECRET, ORG_URL);
    }

    @Test
    public void testReadCookie() throws Exception {
        String cookieVal = "eyJIdWxsLVVzZXItSWQiOiI1MWE2MjkzN2QxMjAxMTdhNjQwMDAwMTMiLCJIdWxsLVVzZXItU2lnIjoiMTM3NDQ2OTA5OC4xY2NmMTUwNmVlZjZhNDcxNzcyODg3ZjhlNjg1NzZiZDk0NmNiODQyIn0%3D";
        Map<String, String> result = filter.decodeCookie(cookieVal);
        Assert.assertEquals(result.get("Hull-User-Id"), HULL_USER_ID);
        Assert.assertEquals(result.get("Hull-User-Sig"), HULL_USER_SIG);
    }

    @Test
    public void testCurrentUserId() throws Exception {
        String userId = filter.currentUserId(HULL_USER_ID, HULL_USER_SIG);
        Assert.assertEquals(userId, HULL_USER_ID);
    }

}
