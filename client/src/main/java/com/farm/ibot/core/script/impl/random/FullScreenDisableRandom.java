package com.farm.ibot.core.script.impl.random;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interact.action.impl.WidgetAction;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.RandomEvent;

public class FullScreenDisableRandom extends RandomEvent {
    public void onStart() {
    }

    public int onLoop() {
        Widgets.closeTopInterface();
        if (Client.getGameConfig().getResizableMode() != 1) {

            WidgetAction.create(17104929).sendByMouse();
        }

        if (Config.get(427) == 1) {

            WidgetAction.create(17104990).sendByMouse();
        }

        return 1000;
    }

    public long checkInterval() {
        return 6000L;
    }

    public boolean isEnabled() {
        return (this.isTutorialCompleted() || Config.get(281) > 3) && (Client.getGameConfig().getResizableMode() != 1 || Config.get(427) == 1);
    }

    public boolean isBackground() {
        return false;
    }

    public boolean isTutorialCompleted() {
        return (new Tile(3139, 3091, 0)).distance() > 120 && (new Tile(3110, 9509, 0)).distance() > 120;
    }
}
