package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.util.Time;

public class InputBox {
    public static InputBox.MakeItemDialogue getSelectedMakeItemOption() {
        InputBox.MakeItemDialogue[] var0 = InputBox.MakeItemDialogue.values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            InputBox.MakeItemDialogue makeItemDialogue = var0[var2];
            if (makeItemDialogue.isSelected()) {
                return makeItemDialogue;
            }
        }

        return null;
    }

    public static boolean isMakeItemDialogueOpen() {
        return getSelectedMakeItemOption() != null;
    }

    public static boolean isOpen() {
        Widget child = getTextWidget();
        return child != null && child.isVisible();
    }

    public static Widget getTextWidget() {
        return Widgets.get(162, 45);
    }

    public static String getText() {
        if (isOpen()) {
            Widget child = getTextWidget();
            if (child != null && child.isVisible()) {
                if (child.getText().contains("col>")) {
                    return child.getText().split("col> ")[1];
                }

                if (child.getText().contains("*")) {
                    if (child.getText().length() > 1) {
                        return child.getText().split("\\*")[0];
                    }

                    return "";
                }
            }
        }

        return "";
    }

    public static boolean input(Object text) {
        return input(text, 300);
    }

    public static boolean input(Object text, int sleepTime) {
        return input(text, sleepTime, true);
    }

    public static boolean input(Object text, int sleepTime, boolean pressEnter) {
        Client.setLockLowCpuUntil(System.currentTimeMillis() + 3200L);
        Time.waitNextGameCycle();
        if (Time.sleep(3000, InputBox::isOpen)) {
            if (getText().contains("" + text)) {
                if (pressEnter) {
                    Keyboard.enter();
                }

                return true;
            }

            if (getText().length() > 1) {
                for (int i = 0; i < getText().length() + 5; ++i) {
                    Keyboard.press(8);
                    Time.sleep(30);
                }
            }

            if (getText().length() < 2) {
                Keyboard.type(text.toString());
                if (isOpen()) {
                    Time.sleep(sleepTime);
                    if (pressEnter) {
                        Keyboard.enter();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public static enum MakeItemDialogue {
        MAKE_1("1", 7),
        MAKE_5("5", 8),
        MAKE_10("10", 9),
        MAKE_X("Other quantity", 11),
        MAKE_ALL("All", 12);

        public int menuIndex;
        public String tooltip;

        private MakeItemDialogue(String tooltip, int menuIndex) {
            this.tooltip = tooltip;
            this.menuIndex = menuIndex;
        }

        public boolean select() {
            Widget w = this.getWidget();
            return this.isSelected() || w != null && w.interact(this.tooltip) && Time.sleep(this::isSelected);
        }

        public boolean selectAndExecute() {
            if (this.select()) {
                Keyboard.type(49);
                Time.sleep(600, 800);
                return true;
            } else {
                return false;
            }
        }

        public boolean selectAndExecute(int index) {
            if (this.select()) {
                Keyboard.type(48 + index);
                Time.sleep(600, 800);
                return true;
            } else {
                return false;
            }
        }

        public boolean isSelected() {
            Widget w = this.getWidget();
            return w != null && (w.getActions() == null || w.getActions().length == 0);
        }

        public Widget getWidget() {
            Widget w = Widgets.get(270, this.menuIndex);
            return w != null ? w.withOffset(0, -23) : null;
        }
    }
}
