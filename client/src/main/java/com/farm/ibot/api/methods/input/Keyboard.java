package com.farm.ibot.api.methods.input;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.SeedRandom;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.Bot;
import com.farm.ibot.init.AccountData;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard {
    public static KeyListener getKeyListener() {
        return (KeyListener) Client.getKeyboard();
    }

    public static KeyEvent create(int id, int keyCode, char c) {
        Bot.get().getCanvasHandler().enableInput();
        return new KeyEvent(Client.getOriginalCanvas(), id, System.currentTimeMillis(), 0, keyCode, c, id != 400 ? 1 : 0);
    }

    public static void press(int c) {
        KeyEvent e = create(401, c, (char) c);
        getKeyListener().keyPressed(e);
        Time.waitNextGameCycle();
    }

    public static void type(int c) {
        KeyEvent e = create(400, 0, (char) c);
        getKeyListener().keyTyped(e);
    }

    public static void release(int c) {
        KeyEvent e = create(402, c, (char) c);
        getKeyListener().keyReleased(e);
        Time.waitNextGameCycle();
    }

    public static void type(String string) {
        SeedRandom typeRandom = new SeedRandom(AccountData.seed());
        int speed = typeRandom.next(70, 200);
        char[] var3 = string.toCharArray();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            char c = var3[var5];
            type(c);
            Time.sleep(speed + Random.next(0, 100));
        }

    }

    public static void enter() {
        press(10);
    }

    public static void typeAndSend(String string) {
        type(string);
        Time.waitNextGameCycle();
        enter();
    }
}
