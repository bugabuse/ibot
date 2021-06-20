package com.farm.ibot.core.script.impl.random;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.listener.EventHandler;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.KeyBindingConfig;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.core.script.RandomEvent;

import java.awt.*;

public class DialogueSkipper extends RandomEvent implements PaintHandler, MessageListener {
    public EventHandler messageListenerHandler = new MessageEventHandler(this);
    public boolean skipLevelUpInterface = false;

    public DialogueSkipper() {
        this.addEventHandler(this.messageListenerHandler);
    }

    public void onPaint(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(10, drawStringY - 20, 170, 80);
        g.setColor(Color.white);
        this.drawString(g, "Dialogue skip event");
    }

    public void onStart() {

    }

    public int onLoop() {
        if (Widgets.forId(18153488) != null) {
            Rectangle bounds = Widgets.forId(18153488).getBounds();
            Mouse.click((int) bounds.getCenterX(), (int) bounds.getCenterY());
        }

        Widget mapInterface = Widgets.forId(38993958);
        if (mapInterface != null) {
            mapInterface.interact("");
        } else {
            if (Dialogue.containsAny(false, "You can now", "If you have a question or are stuck", "If you need some help", "Greetings, adventurer.", "Why not take on a quest or two", "I'm good, thanks.")) {
                Walking.walkTo(Player.getLocal().getPosition(), -1);
                return 1000;
            }

            if (Dialogue.containsAny(false, "In future, only warn about dangerous", "Congratulations, you just advanced a", "warn me about this again", "is currently restricted")) {
                Dialogue.goNext("In future, only warn about dangerous", "warn me about this again");
                return 1000;
            }
        }

        if (Dialogue.containsAny(false, "You must be on a members", "You must be a member to")) {
            Walking.walkTo(Player.getLocal().getPosition(), -1);
            return 1000;
        } else {
            Dialogue.goNext("");
            return 1000;
        }
    }

    public boolean isEnabled() {
        KeyBindingConfig.setDefaults();
        if (Widgets.forId(18153488) != null) {
            return true;
        } else if (Widgets.forId(38993958) != null && Widgets.forId(38993958).isRendered()) {
            return true;
        } else {
            if (this.skipLevelUpInterface) {
                if (Dialogue.containsAny(true, "There is new information", "If you have a question or are stuck", "If you need some help", "Greetings, adventurer.", "Why not take on a quest or two") && Dialogue.canClickContinue()) {
                    return true;
                }
            } else if (Dialogue.containsAny(true, "You can now", "There is new information", "Congratulations, you just advanced a", "If you have a question or are stuck", "is currently restricted", "warn me about this again", "If you need some help", "Greetings, adventurer.", "Why not take on a quest or two") && Dialogue.canClickContinue()) {
                return true;
            }

            return Dialogue.containsAny(false, "In future, only warn about dangerous", "I'm good, thanks.", "You must be on a members", "You must be a member to");
        }
    }

    public boolean isBackground() {
        return false;
    }

    public long checkInterval() {
        return 12000L;
    }

    public void onMessage(String message) {
        if (message.contains("Please finish what you're doing before using")) {
            Walking.walkTo(Player.getLocal().getPosition(), -1);
        }

    }
}
