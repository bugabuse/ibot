package com.farm.ibot.plugins.backgroundupdater;

import com.farm.ibot.Main;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;
import com.farm.ibot.updater.UpdateFile;
import com.farm.ibot.updater.Updater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class OnTheFlyUpdate extends Plugin {
    public void onStart() {
    }

    public int onLoop() {
        try {
            ArrayList<UpdateFile> files = Updater.getFilesToUpdate();
            if (files.size() > 0) {

                if (Updater.updateFiles()) {

                    Iterator var2 = Main.bots.iterator();

                    while (var2.hasNext()) {
                        Bot bot = (Bot) var2.next();
                        if (bot.getScriptHandler().getScript() != null && files.stream().anyMatch((f) -> {
                            return f.localLocation.toLowerCase().contains("" + bot.getScriptHandler().getScript().getName().toLowerCase());
                        })) {
                            bot.getScriptHandler().restart();
                        }
                    }
                }
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return 10000;
    }
}
