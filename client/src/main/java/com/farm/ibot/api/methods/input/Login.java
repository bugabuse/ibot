package com.farm.ibot.api.methods.input;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.Bot;
import com.farm.ibot.init.AccountData;

import java.awt.*;

public class Login {
    public static boolean logout() {
        if (Client.isInGame()) {
            Widgets.closeTopInterface();
            if (!GameTab.LOGOUT.open()) {
                return false;
            } else {
                Widget logout1 = Widgets.get((w) -> {
                    return w.getId() == 11927560;
                });
                Widget logout2 = Widgets.get(69, 23);
                if (logout1 != null && logout1.isVisible()) {
                    logout1.interact("Logout");
                } else if (logout2 != null && logout2.isVisible()) {
                    logout2.interact("Logout");
                }

                return Time.sleep(() -> {
                    return !Client.isInGame();
                });
            }
        } else {
            return true;
        }
    }

    public static void login() {
        login(Bot.get().getSession().getAccount());
    }

    public static void login(AccountData account) {
        if (account == null) {

        } else if (account.password == null) {

            account.isBanned = true;
        } else {
            if (getState() == Login.ScreenState.WORLDHOP) {
                Mouse.click(733, 13);
                Time.waitNextGameCycle();
                Time.sleep(1000);
            }

            if (Client.getLoginState() <= 20) {
                Client.setLoginScreenId(2);
                Client.setLoginAccountState(-1);
                Time.sleep(600);
                Client.setUsername(account.username);
                Client.setPassword(account.password);

                for (int i = 0; i < 3; ++i) {
                    Keyboard.press(10);
                    Time.sleep(1000);
                }
            }

        }
    }

    public static Login.ScreenState getState() {
        if (Client.getLoginState() > 20) {
            return Login.ScreenState.INGAME;
        } else if (Client.getLoginState() <= 20) {
            return colorMatches(new Color(70, 70, 70), 596, 5) ? Login.ScreenState.WORLDHOP : Login.ScreenState.LOGIN;
        } else {
            return Login.ScreenState.UNKNOWN;
        }
    }

    public static boolean colorMatches(Color color, int x, int y) {
        return Screen.getColorAt(x, y).equals(color);
    }

    public static boolean colorMatches(int x, int y) {
        return Screen.getColorAt(x, y).equals(new Color(255, 255, 0));
    }

    public static enum ScreenState {
        UNKNOWN,
        WELCOME,
        LOGIN,
        WORLDHOP,
        INGAME;
    }
}
