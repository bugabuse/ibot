package com.farm.ibot.core.script.impl.random;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.pathfinding.impl.WebPathFinder;
import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.script.RandomEvent;

import java.awt.*;

public class WebNotFoundRandom extends RandomEvent implements PaintHandler {
    public boolean enabled = true;
    public Condition isDisabledCondition;

    public void onStart() {
    }

    public int onLoop() {

        Debug.log("Additional check: " + (this.isDisabledCondition != null && this.isDisabledCondition.active()));
        Magic.LUMBRIDGE_HOME_TELEPORT.select();
        return 20000;
    }

    private boolean isEnabledEarlyCheck() {
        if (!Client.isInGame()) {
            return false;
        } else if (this.isDisabledCondition != null && this.isDisabledCondition.active()) {
            return false;
        } else if (this.enabled && !Dialogue.isInDialouge() && GameTab.MAGIC.getWidget() != null && Client.getLoginState() == 30) {
            WebNode node = WebPathFinder.getClosestReachableNode(Player.getLocal().getPosition());
            return node == null;
        } else {
            return false;
        }
    }

    public boolean isEnabled() {
        boolean enabled = this.isEnabledEarlyCheck();
        if (enabled) {
            return !Time.sleep(40000, () -> {
                return !this.isEnabledEarlyCheck();
            });
        } else {
            return enabled;
        }
    }

    public boolean isBackground() {
        return false;
    }

    public long checkInterval() {
        return 35000L;
    }

    public void onPaint(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(10, drawStringY - 20, 170, 80);
        g.setColor(Color.white);
        this.drawString(g, "Outside the web - random handler");
        this.drawString(g, "Enabled: " + this.enabled);
    }
}
