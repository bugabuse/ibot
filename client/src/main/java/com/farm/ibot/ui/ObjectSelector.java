package com.farm.ibot.ui;

import com.farm.ibot.Main;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.Session;
import com.farm.ibot.init.SessionProfile;

import javax.swing.*;

public class ObjectSelector {
    public static AccountData showAccountSelector() {
        return (AccountData) showAndGet("Choose an account", Session.getAccountsCache());
    }

    public static SessionProfile showProfileSelection() {
        SessionProfile profile = (SessionProfile) showAndGet("Choose a profile session", SessionProfile.getProfiles());
        int amount = Integer.parseInt(JOptionPane.showInputDialog(Main.frame, "Amount of bots?", 1));
        return SessionProfile.multiplySession(profile, amount);
    }

    public static <T> T showAndGet(T... values) {
        return (T) JOptionPane.showInputDialog(Main.frame, "Choose account", "Choose account", 0, (Icon) null, values, values[0]);

    }

    public static <T> T showAndGet(String message, T... values) {
        return (T) JOptionPane.showInputDialog(Main.frame, message, message, 0, (Icon) null, values, values[0]);
    }
}
