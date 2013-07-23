package io.hull;

import org.junit.Assert;
import org.junit.Test;

import java.lang.IllegalArgumentException;

public class HullConfigurationTest {
    private static String APP_ID = "12345";
    private static String APP_SECRET = "abcde";
    private static String ORG_URL = "http://test.hullapp.io";

    @Test
    public void testConstructor() throws Exception {
        HullConfiguration config = new HullConfiguration(APP_ID, APP_SECRET, ORG_URL);
        Assert.assertEquals(config.getAppId(), APP_ID);
        Assert.assertEquals(config.getAppSecret(), APP_SECRET);
        Assert.assertEquals(config.getOrgUrl(), ORG_URL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalid() throws Exception {
        HullConfiguration config = new HullConfiguration(APP_ID, null, ORG_URL);
    }
}
