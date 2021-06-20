package com.farm.ibot.api.wrapper;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.util.Time;

public enum GameTab {
    COMBAT(35913779, 112),
    STATS(35913780, 113),
    QUESTS(35913781, 114),
    INVENTORY(35913782, 123),
    EQUIPMENT(35913783, 115),
    PRAYER(35913784, 116),
    MAGIC(35913785, 117),
    CLAN_CHAT(35913762, 118),
    FRIEND_LIST(35913764, 119),
    ACCOUNT_MANAGEMENT(35913763, 120),
    LOGOUT(35913765),
    OPTIONS(35913766, 121),
    EMOTES(35913767, 122),
    MUSIC(35913768),
    NONE(38);

    public int widgetId;
    public int hotKey;

    private GameTab(int widgetId) {
        this(widgetId, -1);
    }

    private GameTab(int widgetId, int hotKey) {
        this.widgetId = widgetId;
        this.hotKey = hotKey;
    }

    public static GameTab getOpened() {
        GameTab[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            GameTab tab = var0[var2];
            if (tab.isOpen()) {
                return tab;
            }
        }

        return NONE;
    }

    public Widget getWidget() {
        Widget widget = Widgets.forId(this.widgetId);
        return widget != null && widget.isVisible() ? widget : null;
    }

    public boolean isOpen() {
        Widget widget = this.getWidget();
        return widget != null && widget.getTextureId() != -1;
    }

    public boolean isEnabled() {
        return this.getWidget() != null;
    }

    public boolean open() {
        if (this.isOpen()) {
            return true;
        } else if (this.hotKey == -1) {
            return this.openByClick();
        } else {
            Keyboard.press(this.hotKey);
            return Time.sleep(150, () -> {
                return this.isOpen();
            });
        }
    }

    public boolean openByClick() {
        Widget widget = this.getWidget();
        return widget != null && widget.interact("") && Time.sleep(this::isOpen);
    }
}
