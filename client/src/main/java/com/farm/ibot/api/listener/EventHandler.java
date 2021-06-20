package com.farm.ibot.api.listener;

public abstract class EventHandler {
    public Thread listeningThread = null;

    public abstract int listen();
}
