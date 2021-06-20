package com.farm.ibot.plugins.onlinedatasender;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;
import com.farm.ibot.init.Session;
import com.farm.ibot.init.SessionProfile;
import com.farm.ibot.init.Settings;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class LastAccountsSaver extends Plugin {
    public void onStart() {
    }

    public int onLoop() {
        if (Main.bots.size() <= 0) {
            return 30000;
        } else {
            Debug.log("Size " + Main.bots.size());
            if (!Settings.SETTINGS_DIRECTORY.exists()) {
                Settings.SETTINGS_DIRECTORY.mkdirs();
            }

            File file = new File(Settings.SETTINGS_DIRECTORY.getAbsolutePath() + File.separator + "lastaccs.json");
            ArrayList<Session> lastSessions = new ArrayList();
            Iterator var3 = Main.bots.iterator();

            while (var3.hasNext()) {
                Bot bot = (Bot) var3.next();
                String scriptName = bot.getScriptHandler().getScriptLoader().getLastScriptName();
                if (scriptName != null && scriptName.length() > 0) {
                    Debug.log("SAVE " + bot.getSession().getAccount() + " " + scriptName);
                    Session session = new Session(bot.getSession().getAccount(), scriptName);
                    lastSessions.add(session);
                }
            }

            SessionProfile profile = new SessionProfile("Lastly loaded sessions", (Session[]) lastSessions.toArray(new Session[0]));

            try {
                Writer writer = new BufferedWriter(new FileWriter(file));
                (new Gson()).toJson(profile, writer);
                writer.close();
                System.out.println("Saved last session.");
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            return 30000;
        }
    }
}
