package com.farm.botmanager;

import java.io.File;
import java.net.URISyntaxException;

public class Settings {
    public static File FILE_SESSION_PROFILES;
    public static String BOT_DATA_PATH;
    public static File GAMEPACK_FILE;
    public static File BOT_JAR_FILE;
    public static File XBOOT_JAR_FILE;
    public static File OBJECT_DEFINITION_FILE;
    public static File NPC_DEFINITION_FILE;
    public static File ITEM_DEFINITION_FILE;
    public static File WEBDATA_FILE;
    public static File HOOKS_DREAMBOT;
    public static File HOOKS_SIMBA;
    public static File SCRIPTS_DIRECTORY;
    public static boolean isWindows;

    static {
        isWindows = System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static void load() {
        File parentFile = null;
        try {
            parentFile = new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        BOT_DATA_PATH = parentFile.getParentFile().getAbsolutePath() + "/";
        //BOT_DATA_PATH = new File(...);
        BOT_JAR_FILE = new File(BOT_DATA_PATH + "osrsbot.jar");
        XBOOT_JAR_FILE = new File(BOT_DATA_PATH + "xboot.jar");
        GAMEPACK_FILE = new File(BOT_DATA_PATH + "gamepack.jar");
        OBJECT_DEFINITION_FILE = new File(BOT_DATA_PATH + "definitions/ObjectDefinitions.json");
        NPC_DEFINITION_FILE = new File(BOT_DATA_PATH + "definitions/NpcDefinitions.json");
        ITEM_DEFINITION_FILE = new File(BOT_DATA_PATH + "definitions/ItemDefinitions.json");
        FILE_SESSION_PROFILES = new File(BOT_DATA_PATH + "definitions/SessionProfiles.json");
        WEBDATA_FILE = new File(BOT_DATA_PATH + "definitions/WebData.json");
        HOOKS_DREAMBOT = new File(BOT_DATA_PATH + "hooks/dreambot.txt");
        HOOKS_SIMBA = new File(BOT_DATA_PATH + "hooks/simba.txt");
        SCRIPTS_DIRECTORY = new File(BOT_DATA_PATH + "/scripts/");
    }
}

