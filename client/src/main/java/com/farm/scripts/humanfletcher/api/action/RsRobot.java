package com.farm.scripts.humanfletcher.api.action;

import com.farm.ibot.api.accessors.GameShell;
import com.farm.ibot.api.methods.input.Mouse;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RsRobot {
    public static boolean hasFocus = false;

    public void loseFocus() {
        FocusListener[] var1 = GameShell.getInstance().getCanvas().getFocusListeners();
        int var2 = var1.length;

        int var3;
        for (var3 = 0; var3 < var2; ++var3) {
            FocusListener focusListener = var1[var3];
            focusListener.focusLost(new FocusEvent(GameShell.getInstance().getCanvas(), 1005));
        }

        MouseListener[] var5 = GameShell.getInstance().getCanvas().getMouseListeners();
        var2 = var5.length;

        for (var3 = 0; var3 < var2; ++var3) {
            MouseListener listener = var5[var3];
            listener.mouseExited(new MouseEvent(GameShell.getInstance().getCanvas(), 505, System.currentTimeMillis(), 0, 0, 0, 1, false, 0));
        }

        Mouse.getMouse().x = -1;
        Mouse.getMouse().y = -1;
    }

    public void gainFocus() {
        FocusListener[] var1 = GameShell.getInstance().getCanvas().getFocusListeners();
        int var2 = var1.length;

        int var3;
        for (var3 = 0; var3 < var2; ++var3) {
            FocusListener focusListener = var1[var3];
            focusListener.focusGained(new FocusEvent(GameShell.getInstance().getCanvas(), 1004));
        }

        MouseListener[] var5 = GameShell.getInstance().getCanvas().getMouseListeners();
        var2 = var5.length;

        for (var3 = 0; var3 < var2; ++var3) {
            MouseListener listener = var5[var3];
            listener.mouseEntered(new MouseEvent(GameShell.getInstance().getCanvas(), 504, System.currentTimeMillis(), 0, 0, 0, 1, false, 0));
        }

        Mouse.getMouse().x = -1;
        Mouse.getMouse().y = -1;
    }
}
