package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.Bot;

import java.util.ArrayList;
import java.util.HashMap;

public class Dialogue {
    private static int[][] WIDGET_CLICK_CONTINUE = new int[][]{{231, 3}, {229, 2}, {217, 3}, {193, 3}, {11, 4}, {233, 3}, {162, 45}, {193, 0, 2}, {633, 0, 2}};
    private static HashMap<Bot, Long> lastDialogueTime = new HashMap();

    public static boolean talkTo(String npcName) {
        return talkTo(npcName, false);
    }

    public static boolean talkTo(String npcName, boolean walk) {
        return talkTo(Npcs.get(npcName), walk);
    }

    public static boolean talkTo(Npc npc, boolean walk) {
        if (mustClickContinue()) {

            clickContinue();
            return true;
        } else if (isInDialouge()) {

            return true;
        } else {
            if (npc != null) {

                if (walk && !npc.isReachable()) {
                    WebWalking.walkTo(npc.getPosition());
                } else if (npc.interact("Talk-to") && Time.sleep(6000, 1000, Dialogue::isInDialouge)) {
                    return true;
                }
            }

            return isInDialouge();
        }
    }

    public static void goNext(String... options) {
        if (options == null || options.length == 0) {
            options = new String[]{""};
        }

        clickContinue();
        String[] var1 = options;
        int var2 = options.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String myOption = var1[var3];
            if (selectOptionThatContains(myOption)) {
                Time.sleep(600, 800);
                return;
            }
        }

    }

    public static boolean selectOptionThatContains(String value) {
        Client.setLockLowCpuUntil(System.currentTimeMillis() + 600L);
        Time.waitNextGameCycle();
        String[] options = getOptions();

        for (int i = 0; i < options.length; ++i) {
            if (options[i].contains(value) && (!options[i].contains("<str>") || value.contains("<str>"))) {
                Debug.log("Typing: " + (i + 1) + " option name " + options[i]);
                Keyboard.type(49 + i);
                return true;
            }
        }

        return false;
    }

    public static boolean isInDialouge() {
        boolean isInDialogue = isInTheCutScene() || Widgets.get(193, 0, 2) != null || canClickContinue() || getPleaseWaitWidget() != null || Widgets.get(219, 0) != null;
        if (isInDialogue) {
            lastDialogueTime.put(Bot.get(), System.currentTimeMillis());
        }

        return System.currentTimeMillis() - (Long) lastDialogueTime.getOrDefault(Bot.get(), 0L) < 3000L;
    }

    public static boolean isInTheCutScene() {
        return Config.get(1021) != 0 && !GameTab.INVENTORY.isEnabled() && GameTab.FRIEND_LIST.isEnabled();
    }

    public static Widget getContinueWidgetNoText() {
        int[][] var0 = WIDGET_CLICK_CONTINUE;
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            int[] ids = var0[var2];
            Widget w = ids.length > 2 ? Widgets.get(ids[0], ids[1], ids[2]) : Widgets.get(ids[0], ids[1]);
            if (w != null && w.isRendered()) {
                return w;
            }
        }

        return null;
    }

    public static Widget getContinueWidget() {
        Widget w = getContinueWidgetNoText();
        return w != null && "click here to continue".equalsIgnoreCase(w.getText()) ? w : null;
    }

    public static Widget getPleaseWaitWidget() {
        Widget w = getContinueWidgetNoText();
        return w != null && "please wait...".equalsIgnoreCase(w.getText()) ? w : null;
    }

    public static boolean canClickContinue() {
        return getContinueWidget() != null || mustClickContinue();
    }

    public static boolean mustClickContinue() {
        return Widgets.get(162, 45) != null && Widgets.get(162, 45).isRendered();
    }

    public static void clickContinue() {
        Client.setLockLowCpuUntil(System.currentTimeMillis() + 600L);
        Time.waitNextGameCycle();
        if (canClickContinue()) {
            if (mustClickContinue()) {
                Mouse.click(214, 427);
                Keyboard.press(32);
            } else {
                Keyboard.press(32);
            }

            Time.sleep(600);
        }

    }

    public static String getNpcMessage() {
        if (Widgets.isValid(231)) {
            Widget w = Widgets.get(231, 3);
            return w != null ? w.getText() : "";
        } else {
            return "";
        }
    }

    public static String[] getOptions() {
        ArrayList<String> options = new ArrayList();
        Widget widget = Widgets.get(219, 1);
        if (widget != null && widget.getChildren() != null) {
            Widget[] var2 = widget.getChildren();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Widget child = var2[var4];
                if (child.getTextureId() == -1 && child.getScreenY() > 362 && child.getWidth() == 479 && child.getText() != null && child.getText().length() > 0) {
                    options.add(child.getText());
                }
            }
        }

        if (options.size() > 0) {
            options.remove(0);
        }

        return (String[]) options.toArray(new String[options.size()]);
    }

    public static boolean contains(String message) {
        return isInDialouge() && Widgets.get((w) -> {
            return w != null && w.getText() != null && w.getText().contains(message);
        }) != null;
    }

    public static boolean containsAny(boolean checkIfIsInDialogue, String... messages) {
        return (isInDialouge() || !checkIfIsInDialogue) && Widgets.get((w) -> {
            return w != null && w.getText() != null && StringUtils.containsAny(w.getText(), messages);
        }) != null;
    }

    public static void interrupt() {
        Walking.walkTo(Player.getLocal().getPosition(), -1);
    }
}
