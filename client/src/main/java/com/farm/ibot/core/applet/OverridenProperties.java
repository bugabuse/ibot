package com.farm.ibot.core.applet;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.util.web.ProxyRequestThread;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.BotThreadGroup;
import com.farm.ibot.proxy.Proxy;

import java.lang.reflect.Method;
import java.util.Properties;

public class OverridenProperties extends Properties {
    public static void printStackTrace() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();

        for (int i = 0; i < stElements.length; ++i) {
            StackTraceElement ste = stElements[i];

        }


    }

    public static boolean isCalledByClient() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        StackTraceElement[] var1 = stElements;
        int var2 = stElements.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            StackTraceElement ste = var1[var3];
            if (ste.getClassName().startsWith("sun.font.FontUtilities") || ste.getClassName().startsWith("sun.awt.Win32FontManager")) {
                return false;
            }
        }

        return true;
    }

    public String getProperty(String key) {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread thread = Thread.currentThread();
        if (thread instanceof ProxyRequestThread) {
            Proxy proxy = ((ProxyRequestThread) thread).getProxy();
            if (proxy != null) {
                if (key.equals("socksProxyHost")) {
                    return proxy.host;
                }

                if (key.equals("socksProxyPort")) {
                    return "" + proxy.port;
                }
            }
        } else if (threadGroup instanceof BotThreadGroup) {
            Bot bot = ((BotThreadGroup) threadGroup).getBot();

            /**
             *  we have a problem with this on linux platforms.
             */
            /*
            if (bot.spoofedOperatingSystem.properties.containsKey(key) && isCalledByClient()) {
                return (String) bot.spoofedOperatingSystem.properties.get(key);
            }*/

            if (bot.proxy != null && (!key.contains("socksProxy") || Client.getLoginState() != 10)) {
                if (key.equals("socksProxyHost")) {
                    return bot.proxy.host;
                }

                if (key.equals("socksProxyPort")) {
                    return "" + bot.proxy.port;
                }
            }

            if (bot.properties.containsKey(key)) {
                return (String) bot.properties.get(key);
            }
        }

        return super.getProperty(key);
    }

    public void init() {
        try {
            Method m = System.class.getDeclaredMethod("initProperties", Properties.class);
            m.setAccessible(true);
            m.invoke(null, this);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
