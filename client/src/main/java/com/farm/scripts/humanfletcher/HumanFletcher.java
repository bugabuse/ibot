package com.farm.scripts.humanfletcher;

import com.farm.ibot.api.accessors.Camera;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.*;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.impl.debuggers.MouseDebug;
import com.farm.ibot.core.script.impl.debuggers.MouseDebug.ClickPoint;
import com.farm.ibot.init.AccountData;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.Map.Entry;

public class HumanFletcher extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, MouseMotionListener {
    public PaintTimer timer = new PaintTimer();
    public int items;
    public long playTime;
    private SkillTracker tracker;

    public HumanFletcher() {
        super(Strategies.DEFAULT);
        this.tracker = new SkillTracker(Skill.FLETCHING);
    }

    private long getPlayTime() {
        return this.playTime + this.timer.getElapsed();
    }

    public void onStartWhenLoggedIn() {
        this.playTime = AccountData.current().fetchPlayTime(6, 0);
    }

    public void onStart() {

        Bot.getSelectedBot().getScriptHandler().addDebug(MouseDebug.class);
        Strategies.init(this);
        this.getScriptHandler().dialogues.skipLevelUpInterface = true;
        this.getScriptHandler().antiKick.active = false;
        this.addMouseListeners();
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "HumanFletcher Version 0.21");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Last 2h playtime: " + Strategies.accountTermination.minutesInLast2h);
        this.drawString(g, "Last 12h playtime: " + Strategies.accountTermination.minutesInLast12h + "(" + Strategies.accountTermination.minutesInLast12h / 60L + "h)");
        this.drawString(g, this.tracker.getPaintString());
        this.drawString(g, "Items: " + this.items + "(" + this.getItemsPerHour() + ")");
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        this.drawString(g, "Zoom: " + Client.getZoomExact());
        this.drawString(g, "Pitch: " + Camera.getPitch());
        Iterator var2 = MouseDebug.getClicks().entrySet().iterator();

        while (var2.hasNext()) {
            Entry<ClickPoint, Integer> entry = (Entry) var2.next();
            g.setColor(new Color(0, 255, 0, MathUtils.clamp(40 * (Integer) entry.getValue(), 0, 255)));
            g.drawRect(((ClickPoint) entry.getKey()).x, ((ClickPoint) entry.getKey()).y, 1, 1);
        }

        g.setColor(Color.white);
        this.drawString(g, "");
    }

    public boolean setCamera(Tile standTile) {
        if (!MathUtils.isBetween(Camera.getPitch(), 128, 230)) {
            if (Bank.isOpen()) {
                Bank.close();
            }

            Keyboard.press(40);
            Time.sleep(2000, 3000);
            Keyboard.release(40);
            Keyboard.press(40);
            Time.sleep(100, 400);
            Keyboard.release(40);
            return false;
        } else if (MathUtils.isBetween(Client.getZoom(), 600, 1500)) {
            if (standTile.getX() == 3162) {
                if (!MathUtils.isBetween(Camera.getYaw(), 1550, 1600)) {
                    if (Bank.isOpen()) {
                        Bank.close();
                    }

                    Keyboard.press(37);
                    Time.sleep(7000, () -> {
                        return MathUtils.isBetween(Camera.getYaw(), 1550, 1600);
                    });
                    Keyboard.release(37);
                    return false;
                }
            } else if (!MathUtils.isBetween(Camera.getYaw(), 294, 790)) {
                if (Bank.isOpen()) {
                    Bank.close();
                }

                Keyboard.press(37);
                if (Time.sleep(7000, () -> {
                    return MathUtils.isBetween(Camera.getYaw(), 294, 790);
                })) {
                    Keyboard.release(37);
                    Time.sleep(100, 600);
                    Keyboard.press(37);
                    Time.sleep(100, 700);
                    Keyboard.release(37);
                }

                return false;
            }

            return true;
        } else {
            if (Bank.isOpen()) {
                Bank.close();
            }

            Mouse.naturalMove(Random.next(10, Screen.GAME_SCREEN.width - 50), Random.next(10, Screen.GAME_SCREEN.height - 50));
            int zoom = Random.next(27, 40);
            Mouse.scroll(100);
            Time.sleep(800);

            for (int i = 0; i < zoom; ++i) {
                Mouse.scroll(-1);
                Time.sleep(10, 200);
            }

            return false;
        }
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th> <th>Total: " + Strategies.accountTermination.minutesInLast12h + "min (" + Strategies.accountTermination.minutesInLast12h / 60L + "h) </th><th>Fletching: " + Skill.FLETCHING.getRealLevel() + "</th><th>Bows: " + this.getItemsGained() + "(" + this.getItemsPerHour() + ")</th><th>" + this.getCurrentlyExecuting() + "</th>";
    }

    public int loopInterval() {
        return 300;
    }

    private int getItemsGained() {
        return this.items;
    }

    private int getItemsPerHour() {
        return this.timer.getHourRatio(this.items);
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}
