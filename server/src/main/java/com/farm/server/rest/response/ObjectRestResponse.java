/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.rest.response;

public class ObjectRestResponse<T>
extends RestResponse {
    public T object;

    public ObjectRestResponse() {
    }

    public ObjectRestResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public void setState(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void setObject(T obj) {
        this.object = obj;
    }
}

