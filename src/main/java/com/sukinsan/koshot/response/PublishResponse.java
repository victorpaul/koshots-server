package com.sukinsan.koshot.response;

public class PublishResponse {
    private String url;

    public PublishResponse(String http) {
        this.url = http;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
