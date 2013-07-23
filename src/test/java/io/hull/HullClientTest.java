package io.hull;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: crystal
 * Date: 7/21/13
 * Time: 11:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class HullClientTest {
    private static String APP_ID = "12345";
    private static String APP_SECRET = "abcde";
    private static String ORG_URL = "http://test.hullapp.io";
    private static HullClient client;

    @BeforeClass
    public static void setUp() {
        client = new HullClient(APP_ID, APP_SECRET, ORG_URL);
    }

    @Test
    public void testApiPath() throws Exception {
        String path = "12345/app";
        String queryString = "key1=val1&key2=val2";
        String url = client.apiPath(path, queryString);

        Assert.assertEquals(url, ORG_URL + "/api/v1/" + path + "?" + queryString);
    }

    @Test
    public void testApiPathWithLeadingSlash() throws Exception {
        String path = "/12345/app";
        String queryString = "key1=val1&key2=val2";
        String url = client.apiPath(path, queryString);

        Assert.assertEquals(url, ORG_URL + "/api/v1" + path + "?" + queryString);
    }

    @Test
    public void testApiPathWithoutQueryString() throws Exception {
        String path = "12345/app";
        String url = client.apiPath(path, null);

        Assert.assertEquals(url, ORG_URL + "/api/v1/" + path );
    }

    @Test
    public void testApiPathWithEmptyQueryString() throws Exception {
        String path = "12345/app";
        String url = client.apiPath(path, "");

        Assert.assertEquals(url, ORG_URL + "/api/v1/" + path );
    }
}
