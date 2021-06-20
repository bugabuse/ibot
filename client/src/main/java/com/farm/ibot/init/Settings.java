package com.farm.ibot.init;

import com.farm.ibot.api.interact.Interact;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Settings {
    public static boolean lowCpu = false;
    public static boolean disableRendering = true;
    public static boolean devMode = false;
    public static boolean noProxy = false;
    public static boolean autoUpdateEnabled = false;
    public static int renderDelayTime = 40;
    public static boolean processPerTab = false;
    public static boolean cycledSessions = false;
    public static boolean actionInteract = false;
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
    public static File HOOKS_RUNELITE;
    public static File HOOKS_SIMBA;
    public static File UTIL_NAME_DICTIONARY;
    public static File UTIL_NAME_DICTIONARY_PREMIUM;
    public static File API_DATA;
    public static File SETTINGS_DIRECTORY;
    public static File CURSOR_IMAGE;
    public static File EMAILS_TO_CREATE;
    public static File JAV_CONFIG;
    public static File SCRIPTS_DIRECTORY;
    public static int cycleIntervalHours = 6;
    public static int cycleDurationHours = 1;
    public static int cycleDurationMinutes = 30;
    public static boolean useInjection = false;
    public static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
    public static boolean useHumanData = false;
    public static boolean lowCpuEnabled = true;
    public static boolean useLongSleep = true;

    public static void load() {
        useInjection = !ConsoleParams.contains("reflection");
        devMode = ConsoleParams.contains("dev");
        noProxy = ConsoleParams.contains("noproxy");
        autoUpdateEnabled = !ConsoleParams.contains("noupdate");
        cycledSessions = ConsoleParams.contains("cycle");
        useHumanData = ConsoleParams.contains("humandata");
        actionInteract = ConsoleParams.contains("actioninteract");
        lowCpuEnabled = !ConsoleParams.contains("nolowcpu");
        Interact.useNaturalMouse = ConsoleParams.contains("naturalMouse");
        useLongSleep = !ConsoleParams.contains("noLongSleep");
        if (ConsoleParams.contains("renderdelay")) {
            renderDelayTime = ConsoleParams.getIntValue("renderdelay");
        }

        File parentFile = null;

        try {
            parentFile = new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException var3) {
            var3.printStackTrace();
        }

        String dir = parentFile.getParentFile().getAbsolutePath() + "/";
        BOT_DATA_PATH = dir;
        BOT_JAR_FILE = getFile("osrsbot.jar");
        XBOOT_JAR_FILE = getFile("xboot.jar");
        GAMEPACK_FILE = getFile("gamepack.jar");
        OBJECT_DEFINITION_FILE = getFile("definitions/ObjectDefinitions.json");
        NPC_DEFINITION_FILE = getFile("definitions/NpcDefinitions.json");
        ITEM_DEFINITION_FILE = getFile("definitions/ItemDefinitions.json");
        FILE_SESSION_PROFILES = getFile("definitions/SessionProfiles.json");
        WEBDATA_FILE = getFile("definitions/WebData.json");
        HOOKS_DREAMBOT = getFile("hooks/dreambot.txt");
        HOOKS_RUNELITE = getFile("hooks/runelite.txt");
        HOOKS_SIMBA = getFile("hooks/simba.txt");
        SCRIPTS_DIRECTORY = getFile("/scripts/");
        UTIL_NAME_DICTIONARY = getFile("/misc/Dictionary.txt");
        UTIL_NAME_DICTIONARY_PREMIUM = getFile("/misc/Dictionary_premium.txt");
        API_DATA = getFile("/accessors");
        EMAILS_TO_CREATE = getFile("/proxies/emails.txt");
        SETTINGS_DIRECTORY = getFile("/settings/");
        JAV_CONFIG = getFile("/jav_config.txt");
    }

    public static Path getPath(String location) {
        if (location.startsWith("/")) {
            location = location.substring(1);
        }

        return Paths.get(BOT_DATA_PATH + location);
    }

    public static File getFile(String location) {
        if (location.startsWith("/")) {
            location = location.substring(1);
        }
        return new File(BOT_DATA_PATH + location);
    }
}
