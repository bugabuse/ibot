package com.farm.ibot.api.util;

import com.farm.ibot.core.BotThreadGroup;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Debug {
    private static HashMap<String, Boolean> loggers = new HashMap();

    static {
        loggers.put("ScriptHandler", true);
        loggers.put("StrategyScript", false);
        loggers.put("MuleTransferListener", false);
        loggers.put("WalkAction", false);
        loggers.put("Injector", true);
    }

    public static boolean filter(String callerName) {
        if (!loggers.containsKey(callerName)) {
            loggers.put(callerName, true);
        }

        ThreadGroup th = Thread.currentThread().getThreadGroup();
        if (th instanceof BotThreadGroup) {
            /*
            if (Bot.getSelectedBot() == null) {
                return true;
            } else {
                return !th.equals(Bot.getSelectedBot().getThreadGroup()) || isDisabled(callerName);
            }*/
            return isDisabled(callerName);
        } else {
            return false;
        }
    }

    public static void log(Object object) {
        log(object, getCallerClassName());
    }

    public static void log(Object object, String caller) {
        if (!filter(caller)) {
            System.out.println("[" + caller + "] " + object);
        }
    }

    public static void logErr(Object object, String caller) {
        if (!filter(caller)) {
            System.err.println("[" + caller + "] " + object);
        }
    }

    public static void print(Object object) {
        print(object, getCallerClassName());
    }

    public static void print(Object object, String caller) {
        if (!filter(caller)) {
            System.out.print(object);
        }
    }

    public static void printErr(Object object, String caller) {
        if (!filter(caller)) {
            System.err.print(object);
        }
    }

    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();

        for (int i = 1; i < stElements.length; ++i) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Debug.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                String str = ste.getClassName();
                return str.substring(str.lastIndexOf(".") + 1);
            }
        }

        return null;
    }

    private static boolean isDisabled(String debuggerName) {
        return !(Boolean) loggers.getOrDefault(debuggerName, false);
    }

    public static void showDebugsDialog() {
        JPanel al = new JPanel();
        al.setLayout(new GridLayout(18, 18));
        al.setPreferredSize(new Dimension(600, 600));
        Iterator var1 = loggers.entrySet().iterator();

        while (var1.hasNext()) {
            Entry entry = (Entry) var1.next();
            JCheckBox box = new JCheckBox("" + entry.getKey());
            box.setSelected((Boolean) entry.getValue());
            al.add(box);
        }

        JOptionPane.showConfirmDialog((Component) null, al, "Manage debug loggers.", 2);
        Component[] var6 = al.getComponents();
        int var7 = var6.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Component c = var6[var8];
            if (c instanceof JCheckBox) {
                JCheckBox box = (JCheckBox) c;
                loggers.put(box.getText(), box.isSelected());
            }
        }

    }

    public static void printFromGame(String c) {
        printErr(c, "GameEngine");
    }

    public static void printLnFromGame(String line) {
        logErr(line, "GameEngine");
    }
}
