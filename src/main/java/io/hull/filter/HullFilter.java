package io.hull.filter;

import io.hull.HullConfiguration;
import io.hull.HullException;
import io.hull.util.HullUtils;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class HullFilter implements Filter {
    public static final String HULL_USER_ID_KEY = "Hull-User-Id";
    private static final String HULL_USER_SIG_KEY = "Hull-User-Sig";
    private static final String HULL_COOKIE_PREFIX = "hull_";
    private String hullCookie;
    private HullConfiguration config;

    public HullFilter(String appId, String appSecret, String orgUrl) {
        this(new HullConfiguration(appId, appSecret, orgUrl));
    }

    public HullFilter(HullConfiguration config) {
        super();
        this.config = config;
        this.hullCookie = HULL_COOKIE_PREFIX + config.getAppId();
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        req.setAttribute(HULL_USER_ID_KEY, authenticateUser(req));
        chain.doFilter(request, response);
    }

    public void destroy() {
    }


    /**
     * Authenticates the user via cookies
     * @param request
     * @return user ID if valid, otherwise returns null
     */
    public String authenticateUser(HttpServletRequest request) {
        String rval = null;

        String cookieVal = HullUtils.getCookieValue(request.getCookies(), hullCookie);
        Map<String, String> values = decodeCookie(cookieVal);
        if(values != null) {
            rval = currentUserId(values.get(HULL_USER_ID_KEY), values.get(HULL_USER_SIG_KEY));
        }

        return rval;
    }

    /**
     * Decode JSON cookie value and convert to Map
     * @param cookie
     * @return Map of decoded cookie values if valid
     */
    protected static Map<String, String> decodeCookie(String cookie) {
        Map<String, String> rval = null;

        try {
            if(cookie != null) {
                byte[] decoded = Base64.decodeBase64(cookie);
                String json = new String(decoded, "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> values = mapper.readValue(json, Map.class);
                rval = values;
            }
        }
        catch (Exception e) {
            throw new HullException("Could not decode cookie: "+cookie, e);
        }
        return rval;
    }

    /**
     * Returns the current user id if the signature provided is valid
     * @param userId
     * @param userSignature
     * @return
     */
    protected String currentUserId(String userId, String userSignature) {
        if(userId == null || userSignature == null){
            return null;
        }

        String[] props = userSignature.split("\\.");
        String time = props[0];
        String signature = props[1];

        String data = time + '-' + userId;
        String digest = HullUtils.calculateDigest(data, config.getAppSecret());
        String rval = null;
        if(signature.equals(digest)) {
            rval = userId;
        }
        return rval;
    }
}
