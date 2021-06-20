/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.rest.response;

public class RestResponse {
    public boolean success;
    public String message;

    public RestResponse() {
    }

    public RestResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void setState(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}

