package io.hull;


public class HullConfiguration {
    private String appId;
    private String appSecret;
    private String orgUrl;

    public HullConfiguration(String appId, String appSecret, String orgUrl) throws IllegalArgumentException {
        this.appId = appId;
        this.appSecret = appSecret;
        this.orgUrl = orgUrl;
        validate();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getOrgUrl() {
        return orgUrl;
    }

    public void setOrgUrl(String orgUrl) {
        this.orgUrl = orgUrl;
    }

    private void validate() throws IllegalArgumentException {
        if(appId == null) {
            throw new IllegalArgumentException("An appId must be configured");
        }
        else if(appSecret == null) {
            throw new IllegalArgumentException("An appId must be configured");
        }
        else if(orgUrl == null) {
            throw new IllegalArgumentException("An appId must be configured");
        }
    }
}