package io.hull;

import io.hull.util.HullUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class HullClient {
    private static ObjectMapper mapper = new ObjectMapper();
    private HullConfiguration config;
    private HttpClient httpClient;

    public HullClient(HullConfiguration config) {
        this.config = config;
        this.httpClient = new DefaultHttpClient();
    }
    public HullClient(String appId, String appSecret, String orgUrl) {
        this(new HullConfiguration(appId, appSecret, orgUrl));
    }

    /**
     *
     * @param path
     * @return
     */
    public String get(String path) {
        return get(path, null);
    }

    /**
     *
     * @param path
     * @param params Query parameters to be appended to the path
     * @return
     */
    public String get(String path, Map<String,Object> params) {
        String queryString = null;

        if(params != null) {
            Iterator<String> iter = params.keySet().iterator();
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            while(iter.hasNext()) {
                String key = iter.next();
                pairs.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
            }
            queryString = URLEncodedUtils.format(pairs, "utf-8");
        }

        HttpGet request = new HttpGet(apiPath(path, queryString));
        setDefaultHeaders(request);

        return executeRequest(request);
    }

    /**
     *
     * @param path
     * @return
     */
    public String delete(String path) {
        return delete(path, null);
    }
    /**
     *
     * @param path
     * @param params  Query parameters to be appended to the path
     * @return
     */
    public String delete(String path, Map<String,Object> params) {
        String queryString = null;

        if(params != null) {
            Iterator<String> iter = params.keySet().iterator();
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            while(iter.hasNext()) {
                String key = iter.next();
                pairs.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
            }
            queryString = URLEncodedUtils.format(pairs, "utf-8");
        }

        HttpDelete request = new HttpDelete(apiPath(path, queryString));
        setDefaultHeaders(request);

        return executeRequest(request);
    }

    /**
     *
     * @param path
     * @return
     */
    public String post(String path) {
        return post(path, null);
    }

    /**
     *
     * @param path
     * @param params Body params to be included with the request
     * @return
     */
    public String post(String path, Map<String, Object> params) {
        HttpPost request = new HttpPost(apiPath(path, null));
        setDefaultHeaders(request);

        try {
            String x = mapper.writeValueAsString(params);
            request.setEntity(new StringEntity(x));
        } catch(Exception e) {
            throw new HullException("Could not convert params to json string", e);
        }

        return executeRequest(request);
    }

    /**
     *
     * @param path
     * @return
     */
    public String put(String path) {
        return put(path, null);
    }

    /**
     *
     * @param path
     * @param params Body params to be included with the request
     * @return
     */
    public String put(String path, Map<String, Object> params) {
        HttpPut request = new HttpPut(apiPath(path, null));
        setDefaultHeaders(request);

        try {
            String x = mapper.writeValueAsString(params);
            request.setEntity(new StringEntity(x));
        } catch(Exception e) {
            throw new HullException("Could not convert params to json string", e);
        }

        return executeRequest(request);
    }

    /**
     * Executes the HttpRequestBase and returns the response as a String
     * @param request
     * @return String representation of API response
     */
    public String executeRequest(HttpRequestBase request) {
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            throw new HullException("Could not execute Hull request");
        }
    }

    /**
     * Constructs the full Hull API request URL
     * @param path
     * @param queryString
     * @return String of full request URL
     */
    protected String apiPath(String path, String queryString) {
        if(path!=null && path.length()>0 && !path.startsWith("/")) {
            path = "/" + path;
        }
        StringBuilder rval = new StringBuilder();
        rval.append(config.getOrgUrl()).append("/api/v1").append(path);
        if (null != queryString && !queryString.isEmpty()) {
            rval.append("?").append(queryString);
        }
        return rval.toString();
    }

    /**
     * Add default Hull headers to the request
     * @param req
     */
    private void setDefaultHeaders(HttpRequestBase req) {
        req.addHeader("Content-Type", "application/json");
        req.addHeader("Hull-App-Id", config.getAppId());
        req.addHeader("Hull-Access-Token", config.getAppSecret());
        req.addHeader("User-Agent", "Hull Java Client v");
    }

    /**
     * Generate a Hull user hash - used for the "Bring your own users" feature.
     * @param userInfo should at least include "id" and "email" as keys
     * @return String of the user hash
     * @throws IOException
     */
    public String generateUserHash(Map<String,Object> userInfo) throws IOException {
        if(userInfo==null) {
            return null;
        }

        // Timestamp
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);

        // Convert to json and base-64 encode
        String userJson = mapper.writeValueAsString(userInfo);
        String message = Base64.encodeBase64String(userJson.getBytes());

        // HMAC SHA1
        String digest = HullUtils.calculateDigest(message + " " + timestamp, config.getAppSecret());

        StringBuilder builder = new StringBuilder();
        builder.append(message).append(" ");
        builder.append(digest).append(" ");
        builder.append(timestamp);
        return builder.toString();
    }
}