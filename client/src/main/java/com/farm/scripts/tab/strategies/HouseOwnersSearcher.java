package com.farm.scripts.tab.strategies;

import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.Strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HouseOwnersSearcher extends Strategy implements MessageListener {
    public static ArrayList<String> availableHouses = new ArrayList();
    public static HashMap<String, PaintTimer> bannedHouses = new HashMap();
    public static String currentHouse = null;

    public HouseOwnersSearcher(MultipleStrategyScript script) {
        script.addEventHandler(new MessageEventHandler(this));
    }

    public static void banCurrentHouse() {
        bannedHouses.put(currentHouse, new PaintTimer());
        availableHouses.remove(currentHouse);
        currentHouse = null;
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (currentHouse == null) {
            if (availableHouses.size() > 0) {
                currentHouse = (String) availableHouses.get(0);
            } else {
                ArrayList<String> messages = MessageEventHandler.getMessages();

                for (int i = 0; i < 10 && i < messages.size(); ++i) {
                    String message = (String) messages.get(i);

                    String ownerName = null;
                    Pattern pattern = Pattern.compile("(\\[(.*?)])|(\"(.*?)\")|\\((.*?)\\)");
                    Matcher m = pattern.matcher(message);
                    if (m.find()) {
                        ownerName = m.group(0);
                    }

                    if (ownerName != null) {
                        if (bannedHouses.containsKey(ownerName)) {
                            if (((PaintTimer) bannedHouses.get(ownerName)).getElapsedSeconds() > 120L) {
                                bannedHouses.remove(ownerName);
                            }
                        } else if (!availableHouses.contains(ownerName)) {
                            availableHouses.add(ownerName);
                        }
                    }
                }

                this.sleep(1200, 1500);
            }
        }
    }

    public boolean isBackground() {
        return true;
    }

    public void onMessage(String message) {
        if (message.contains("That player is offline") || message.contains("They do not seem to be at home") || message.contains("They have locked their house")) {
            banCurrentHouse();
        }

    }
}
