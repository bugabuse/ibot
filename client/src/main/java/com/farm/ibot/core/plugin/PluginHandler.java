package com.farm.ibot.core.plugin;

import com.farm.ibot.api.util.Time;

import java.util.ArrayList;

public class PluginHandler {
    private ArrayList<Plugin> plugins = new ArrayList();

    public void addPlugin(Plugin plugin) {
        this.plugins.add(plugin);
        this.startExecuting(plugin);
    }

    private void startExecuting(Plugin plugin) {
        (new Thread(() -> {
            plugin.onStart();

            while (true) {
                try {
                    int i = plugin.onLoop();
                    if (i > 0) {
                        Time.sleep(i);
                    }
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

                Time.sleep(10);
            }
        }, "Plugin handler thread")).start();
    }

    public ArrayList<Plugin> getPlugins() {
        return this.plugins;
    }
}
