package com.farm.botmanager.web;

import java.io.IOException;
import java.net.HttpURLConnection;

public class WebResponse {
    private String body;
    private int responseCode;
    private HttpURLConnection connection;

    public WebResponse() {
    }

    public WebResponse(String body) {
        this.body = body;
    }

    public WebResponse(String body, int responseCode) {
        this.body = body;
        this.responseCode = responseCode;
    }

    public WebResponse(String body, HttpURLConnection connection) {
        this.body = body;
        this.connection = connection;
        try {
            this.responseCode = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public HttpURLConnection getConnection() {
        return this.connection;
    }

    public String toString() {
        return this.body != null ? this.body : "";
    }
}

