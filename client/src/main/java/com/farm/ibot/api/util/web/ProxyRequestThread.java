package com.farm.ibot.api.util.web;

import com.farm.ibot.proxy.Proxy;

public class ProxyRequestThread extends Thread {
    private final Proxy proxy;
    public Throwable throwable;

    public ProxyRequestThread(ThreadGroup group, Proxy proxy, Runnable runnable) {
        super(group, runnable);
        this.proxy = proxy;
        this.setUncaughtExceptionHandler(new ProxyRequestThread.StackTraceInheritingUncaughtExceptionHandler());
        this.throwable = new Throwable("Started here");
    }

    public ProxyRequestThread(ThreadGroup group, String proxy, Runnable runnable) {
        this(group, proxy != null && proxy.length() > 1 ? new Proxy(proxy) : null, runnable);
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    private class StackTraceInheritingUncaughtExceptionHandler implements UncaughtExceptionHandler {
        private StackTraceInheritingUncaughtExceptionHandler() {
        }

        // $FF: synthetic method
        StackTraceInheritingUncaughtExceptionHandler(Object x1) {
            this();
        }

        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace(System.err);
            ProxyRequestThread.this.throwable.printStackTrace();
        }
    }
}
