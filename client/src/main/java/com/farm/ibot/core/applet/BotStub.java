package com.farm.ibot.core.applet;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.HashMap;

public class BotStub implements AppletStub {
    private Applet applet;
    private HashMap<String, String> parameters;
    private URL baseUrl;

    public BotStub(Applet applet, URL baseUrl, HashMap<String, String> parameters) {
        this.applet = applet;
        this.baseUrl = baseUrl;
        this.parameters = parameters;
    }

    public boolean isActive() {
        return this.applet.isActive();
    }

    public URL getDocumentBase() {
        return this.baseUrl;
    }

    public URL getCodeBase() {
        return this.baseUrl;
    }

    public String getParameter(String name) {
        return (String) this.parameters.get(name);
    }

    public AppletContext getAppletContext() {
        return null;
    }

    public void appletResize(int width, int height) {
    }
}
