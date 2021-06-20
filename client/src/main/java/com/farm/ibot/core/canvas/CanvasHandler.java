package com.farm.ibot.core.canvas;

import com.farm.ibot.Main;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Region;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BackgroundScript;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.ScriptHandler;
import com.farm.ibot.init.Settings;

import java.awt.*;
import java.util.Iterator;

public class CanvasHandler {
    private final Bot bot;
    public long inputEnabledUntil = 0L;
    public long currentCycle = 0L;
    private long lastLoad;

    public CanvasHandler(Bot bot) {
        this.bot = bot;
    }

    public void enableInput() {
        this.inputEnabledUntil = System.currentTimeMillis() + 2000L;
        Client.setInputEnabled(true);
    }

    public void enableInputMouse() {
        this.inputEnabledUntil = System.currentTimeMillis() + 550L;
        Client.setInputEnabled(true);
    }

    public void paint(Object g) {
        this.paint((Graphics) g);
    }

    public void paint(Graphics g) {
        ++this.currentCycle;
        if (Client.getLoginState() != 30) {
            this.lastLoad = System.currentTimeMillis();
        }

        if (Settings.lowCpuEnabled) {
            if (this.bot.getScriptHandler().getCurrentlyExecuting() != null) {
                if (Settings.useInjection) {
                    if (Settings.lowCpu) {
                        if (Settings.useLongSleep && Player.getLocal() != null && Player.getLocal().getAnimation() != -1) {
                            Bot.get().properties.put("longSleep", "1");
                        } else {
                            Bot.get().properties.put("longSleep", "0");
                        }

                        if (Client.isInGame()) {
                            Client.setLowCpu(true);
                        } else {
                            Client.setLowCpu(false);
                            Time.sleep(600);
                        }

                        Time.sleep(20);
                    } else {
                        Client.setLowCpu(false);
                    }
                } else if (Config.get(144) != 60 && Config.get(2633) != 259 && Config.get(2633) != 258) {
                    if (Settings.lowCpu) {
                        if (Client.isInGame()) {
                            Time.sleep(Settings.renderDelayTime);
                            if (Settings.useLongSleep && Player.getLocal() != null && Player.getLocal().getAnimation() != -1) {
                                Time.sleep(400);
                            }

                            Region.getRegion().setMinLevel(3);
                        } else {
                            Time.sleep(600);
                        }
                    } else {
                        Region.getRegion().setMinLevel(0);
                    }
                }
            } else {
                Client.setLowCpu(false);
            }
        }

        if (this.bot.equals(Main.frame.selectedBot)) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            ScriptHandler scriptHandler = this.bot.getScriptHandler();
            BotScript.resetPaint();
            Iterator var3;
            if (scriptHandler.getCurrentlyExecuting() != null) {
                var3 = scriptHandler.getCurrentlyExecuting().getPaintHandlers().iterator();

                while (var3.hasNext()) {
                    PaintHandler paint = (PaintHandler) var3.next();
                    paint.onPaint(g);
                }
            }

            var3 = scriptHandler.getBackgroundScripts().iterator();

            while (var3.hasNext()) {
                BackgroundScript script = (BackgroundScript) var3.next();
                Iterator var5 = script.getPaintHandlers().iterator();

                while (var5.hasNext()) {
                    PaintHandler paint = (PaintHandler) var5.next();
                    paint.onPaint(g);
                }
            }

        }
    }
}
