package com.farm.ibot.core.script;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.listener.EventHandler;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.manifest.ScriptManifest;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

public abstract class BotScript {
    public static int drawStringY = 30;
    public static int drawStringYLeft = 30;
    public String startArguments = "";
    public ScriptHandler scriptHandler;
    public boolean firstLoginHandled = false;
    public boolean onStartHandled = false;
    protected ArrayList<PaintHandler> paintHandlers = new ArrayList();
    private ArrayList<EventHandler> eventHandlers = new ArrayList();

    public BotScript() {
        if (this instanceof PaintHandler) {
            this.paintHandlers.add((PaintHandler) this);
        }

    }

    public static void resetPaint() {
        drawStringY = 30;
        drawStringYLeft = 30;
    }

    public static BotScript get() {
        return Bot.get().getScriptHandler().getScript();
    }

    public abstract void onStart();

    public void onStartWhenLoggedIn() {
    }

    public void onLoad() {
    }

    public abstract int onLoop();

    public void onStop() {
    }

    public void addMouseListeners() {
        if (this instanceof MouseListener) {
            Client.getOriginalCanvas().addMouseListener((MouseListener) this);
        }

        if (this instanceof MouseMotionListener) {

            Client.getOriginalCanvas().addMouseMotionListener((MouseMotionListener) this);
        }

    }

    public void removeMouseListeners() {
        if (this instanceof MouseListener) {
            Client.getOriginalCanvas().removeMouseListener((MouseListener) this);
        }

        if (this instanceof MouseMotionListener) {
            Client.getOriginalCanvas().removeMouseMotionListener((MouseMotionListener) this);
        }

    }

    public void addEventHandler(EventHandler handler) {
        this.eventHandlers.add(handler);
    }

    public ArrayList<EventHandler> getEventHandlers() {
        return this.eventHandlers;
    }

    public ArrayList<PaintHandler> getPaintHandlers() {
        return this.paintHandlers;
    }

    public void drawStringRight(Graphics g, String str) {
        Color lastColor = g.getColor();
        g.setColor(Color.BLACK);
        int x = 430;
        if (615 + g.getFontMetrics().stringWidth(str) > Screen.CLIENT_SCREEN.width) {
            x = Screen.CLIENT_SCREEN.width - 15 - g.getFontMetrics().stringWidth(str);
        }

        g.drawString(str, x, drawStringYLeft + 1);
        g.setColor(lastColor);
        g.drawString(str, x, drawStringYLeft);
        drawStringYLeft += 18;
    }

    public void drawString(Graphics g, String str) {
        Color lastColor = g.getColor();
        g.setColor(Color.BLACK);
        g.drawString(str, 16, drawStringY + 1);
        g.setColor(lastColor);
        g.drawString(str, 15, drawStringY);
        drawStringY += 18;
    }

    public void drawString(Graphics g, Color color, String str) {
        Color lastColor = g.getColor();
        g.setColor(color);
        this.drawString(g, str);
        g.setColor(lastColor);
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String toString() {
        return this.getName();
    }

    public ScriptHandler getScriptHandler() {
        return this.scriptHandler;
    }

    public String getStartArguments() {
        return this.startArguments;
    }

    public boolean breaksEnabled() {
        return false;
    }

    public ScriptManifest getScriptManifest() {
        if (this.getClass().getDeclaredAnnotation(ScriptManifest.class) != null) {
            return (ScriptManifest) this.getClass().getDeclaredAnnotation(ScriptManifest.class);
        } else {
            ScriptManifest def = new ScriptManifest() {
                public Class<? extends Annotation> annotationType() {
                    return null;
                }

                public boolean isP2p() {
                    return false;
                }
            };
            return def;
        }
    }
}
