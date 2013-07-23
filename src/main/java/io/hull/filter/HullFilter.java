package io.hull.filter;

import io.hull.HullConfiguration;
import io.hull.HullException;
import io.hull.util.HullUtils;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class HullFilter implements Filter {
    public static final String HULL_USER_ID_KEY = "Hull-User-Id";
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

        String cookieVal = HullUtils.getURLDecodedCookieValue(req.getCookies(), hullCookie);
        req.setAttribute(HULL_USER_ID_KEY, HullUtils.authenticateUser(cookieVal, config.getAppSecret()));
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
