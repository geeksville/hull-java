package io.hull.util;

import io.hull.HullException;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.security.Key;
import java.util.Map;

public class HullUtils {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Calculate the HMAC SHA-1 Hex digest for the given input
     * @param data
     * @return
     */
    public static String calculateDigest(String data, String secret) {
        String result = null;
        try {
            Key signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Hex.encodeHexString(rawHmac);

        } catch (Exception e) {
            throw new HullException("Could not calculate digest", e);

        }
        return result;
    }

    /**
     * Retrieves the cookie value for the given cookie name
     * @param cookies
     * @param cookieName
     * @return the cookie value if present, or null if not present
     */
    public static String getCookieValue(Cookie[] cookies, String cookieName) {
        for(int i=0; i<cookies.length; i++) {
            Cookie c = cookies[i];
            if(c.getName().equals(cookieName)) {
                return c.getValue();
            }
        }
        return null;
    }

    /**
     * Converts a json string to a Map
     * @param input
     * @return
     * @throws IOException
     */
    public static Map<String, Object> toMap(String input) throws IOException {
        Map<String, Object> rval = null;
        rval = mapper.readValue(input.getBytes(), new TypeReference<Map<String,Object>>() {});
        return rval;
    }
}
