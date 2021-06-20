package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.init.Settings;

public enum Magic {
    LUMBRIDGE_HOME_TELEPORT(5),
    WIND_STRIKE(6),
    WATER_STRIKE(9),
    EARTH_STRIKE(11),
    FIRE_STRIKE(13),
    CONFUSE(7),
    LVL_1_ENCHANT(8),
    WEAKEN(12),
    CURSE(16),
    TELEKINETIC_GRAB(24),
    TELEPORT_FALADOR(26),
    HIGH_ALCH(39);

    public int widgetId;

    private Magic(int widgetId) {
        this.widgetId = widgetId;
    }

    public String toString() {
        return this.name().toLowerCase().replaceAll("_", " ").replaceAll("-", " ");
    }

    public boolean isSelected() {
        return Client.getSelectedSpell().replaceAll("-", " ").equalsIgnoreCase(this.toString());
    }

    public boolean select() {
        if (this.isSelected()) {
            return true;
        } else if (!GameTab.MAGIC.open()) {
            return false;
        } else {
            Widget widget;
            if (this == LUMBRIDGE_HOME_TELEPORT && Settings.actionInteract) {
                widget = Widgets.get(218, this.widgetId);
                return widget != null && widget.interact("Cast-lumbridge");
            } else {
                widget = Widgets.get(218, this.widgetId);
                return widget != null && widget.interact("Cast") && Time.sleep(this::isSelected);
            }
        }
    }

    public boolean click() {
        if (!GameTab.MAGIC.open()) {
            return false;
        } else {
            Widget widget;
            if (this == LUMBRIDGE_HOME_TELEPORT && Settings.actionInteract) {
                widget = Widgets.get(218, this.widgetId);
                return widget != null && widget.interact("Cast-lumbridge");
            } else {
                widget = Widgets.get(218, this.widgetId);
                return widget != null && widget.interact("Cast");
            }
        }
    }
}
