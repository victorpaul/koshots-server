package com.sukinsan.koshot.response;

public class PublishHttpResponse {
    private String url;

    public PublishHttpResponse(String http) {
        this.url = http;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
