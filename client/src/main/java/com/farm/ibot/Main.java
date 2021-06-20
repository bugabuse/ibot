package com.farm.ibot;

import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.data.definition.NpcDefinition;
import com.farm.ibot.api.data.definition.ObjectDefinition;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.interact.impl.HybridInteractHandler2;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.webwalking.WebData;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.BotThreadGroup;
import com.farm.ibot.core.applet.OverridenProperties;
import com.farm.ibot.core.plugin.PluginHandler;
import com.farm.ibot.init.*;
import com.farm.ibot.plugins.backgroundupdater.OnTheFlyUpdate;
import com.farm.ibot.plugins.cyclemanager.CycleManager;
import com.farm.ibot.plugins.fpsdata.FpsDataRecorder;
import com.farm.ibot.plugins.idleproxyfetcher.IdleProxyFetcher;
import com.farm.ibot.plugins.muleutilsupdater.MuleUtilsUpdater;
import com.farm.ibot.plugins.onlinedatasender.BannedAccountsHandler;
import com.farm.ibot.plugins.onlinedatasender.LastAccountsSaver;
import com.farm.ibot.plugins.onlinedatasender.OnlineDataSender;
import com.farm.ibot.plugins.proxydatasender.ProxyDataSender;
import com.farm.ibot.plugins.scriptstarter.ScriptStarter;
import com.farm.ibot.proxy.SocksAuthenticator;
import com.farm.ibot.ui.BotFrame;
import com.farm.ibot.ui.BotFrameController;
import com.farm.ibot.updater.Updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    public static BotFrame frame = new BotFrame();
    public static ArrayList<Bot> bots = new ArrayList();
    public static PluginHandler pluginHandler = new PluginHandler();
    public static int crashCount = 0;
    public static int totalBotsCount = 0;
    public static String cpuName = "";
    private static String computerName;
    private static LinkedList<Runnable> botStartQueue = new LinkedList();

    public static int inverse(Number input) {
        if (input != null && input.longValue() != -1L) {
            BigInteger a = BigInteger.valueOf(input.longValue());
            BigInteger modulus = BigInteger.ONE.shiftLeft(32);
            return a.modInverse(modulus).intValue();
        } else {
            return -1;
        }
    }

    public static String getDescriptorForClass(Class c) {
        if (c.isPrimitive()) {
            if (c == Byte.TYPE) {
                return "B";
            } else if (c == Character.TYPE) {
                return "C";
            } else if (c == Double.TYPE) {
                return "D";
            } else if (c == Float.TYPE) {
                return "F";
            } else if (c == Integer.TYPE) {
                return "I";
            } else if (c == Long.TYPE) {
                return "J";
            } else if (c == Short.TYPE) {
                return "S";
            } else if (c == Boolean.TYPE) {
                return "Z";
            } else if (c == Void.TYPE) {
                return "V";
            } else {
                throw new RuntimeException("Unrecognized primitive " + c);
            }
        } else {
            return c.isArray() ? c.getName().replace('.', '/') : ('L' + c.getName() + ';').replace('.', '/');
        }
    }

    public static String getMethodDescriptor(Method m) {
        String s = "(";
        Class[] var2 = m.getParameterTypes();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Class c = var2[var4];
            s = s + getDescriptorForClass(c);
        }

        s = s + ')';
        return s + getDescriptorForClass(m.getReturnType());
    }

    public static void initProperties() throws IOException {
        Settings.load();
        Authenticator.setDefault(new SocksAuthenticator());
        OverridenProperties properties = new OverridenProperties();
        properties.init();
        System.setProperties(properties);

        /*
        try {
            if (ConsoleParams.contains("premiumNames")) {
                NameGenerator.loadPremium();
            } else {
                NameGenerator.load();
            }
        } catch (Exception var2) {
        }*/

    }

    public static int fromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static String getHTML(String urlToRead) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer ZtynLkhOkh0snRlwxGgQRhkYwX4xEd2Ojjlem3b1TmSaossPn2QH5sPUTzvisepq");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();
            return result.toString();
        } catch (Exception var6) {
            var6.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        ConsoleParams.args = args;
        initProperties();
        System.setProperty("lowcpu", "false");

        if (ConsoleParams.contains("updatelist")) {
            Settings.load();
            Updater.createUpdateInfo();
            Updater.pushUpdatesToServer();
            Time.sleep(3000);
            System.exit(0);
        } else {
            Debug.log("Using " + (Settings.useInjection ? "injection" : "reflection"));
            if (Settings.actionInteract) {
                Interact.interactHandler = new HybridInteractHandler2();
            }

            if (Settings.devMode) {
                Settings.autoUpdateEnabled = false;
            }

            if (Settings.autoUpdateEnabled) {
                Updater.createUpdateInfo();
                Updater.updateFiles();
            }

            //NameGenerator.load();
            SessionProfile.load();
            ConsoleInputListener.init();
            frame.load();
            BotFrameController.updateButtons();
            WebData.load();
            Hooks.load();
            ObjectDefinition.load();
            NpcDefinition.load();
            ItemDefinition.load();
            if (Settings.autoUpdateEnabled) {
                pluginHandler.addPlugin(new OnTheFlyUpdate());
            }

            pluginHandler.addPlugin(new OnlineDataSender());
            pluginHandler.addPlugin(new ScriptStarter());
            pluginHandler.addPlugin(new BannedAccountsHandler());
            pluginHandler.addPlugin(new MuleUtilsUpdater());
            pluginHandler.addPlugin(new CycleManager());
            pluginHandler.addPlugin(new FpsDataRecorder());
            pluginHandler.addPlugin(new ProxyDataSender());
            if (ConsoleParams.contains("lastaccs")) {
                pluginHandler.addPlugin(new LastAccountsSaver());
            }

            if (ConsoleParams.contains("dynamicProxy")) {
                pluginHandler.addPlugin(new IdleProxyFetcher());
            }

            if (ConsoleParams.contains("process_per_tab")) {
                Settings.processPerTab = true;
            }

            if (ConsoleParams.contains("s")) {
                String sessionIndex = ConsoleParams.getValue("s");
                addBots(SessionProfile.forDescription(sessionIndex), ConsoleParams.getIntValue("accountindex"));
            }

            if (ConsoleParams.contains("lastaccs")) {

                SessionProfile profile = SessionProfile.fromSettingsFile();
                if (profile != null) {
                    System.out.println("Sessions amount: " + profile.sessions.length);
                    addBots(profile, ConsoleParams.getIntValue("accountindex"));
                } else {
                    System.out.println("SessionProfile could not be loaded.");
                }
            }

            (new Thread(() -> {
                while (true) {
                    if (botStartQueue.size() > 0) {
                        Runnable r = (Runnable) botStartQueue.removeFirst();
                        if (r != null) {
                            r.run();
                        }
                    }

                    Time.sleep(100);
                }
            })).start();
        }
    }

    public static void addBots(SessionProfile profile, int accountIndex) {
        Settings.lowCpu = true;
        if (accountIndex != -1) {
            addNewBot(profile.sessions[accountIndex % profile.sessions.length]);
        } else {
            int idx = 0;
            Session[] var3 = profile.sessions;
            int var4 = var3.length;

            label43:
            for (int var5 = 0; var5 < var4; ++var5) {
                Session session = var3[var5];
                Iterator var7 = bots.iterator();

                while (var7.hasNext()) {
                    Bot bot = (Bot) var7.next();
                    if (bot.getSession() != null && bot.getSession().getAccount() != null && session.getAccount() != null && bot.getSession().getAccount().equals(session.getAccount())) {
                        continue label43;
                    }
                }

                if (Settings.processPerTab && idx > 0) {
                    addNewBotProcess(profile, idx);
                    Time.sleep(2000);
                } else {
                    addNewBot(session);
                }

                ++idx;
            }
        }

    }

    public static void addNewBotProcess(SessionProfile profile, int accountIndex) {

        String sessionName = profile.toString();
        Debug.log("Shit here: " + ConsoleParams.getArgsString());

        try {
            String command = "-Xmx256m -noupdate -s " + sessionName + " -accountindex " + accountIndex + " -dynamicProxy -reflection -noactioninteract -renderdelay 50";

            executeCommand(command);

        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static Bot addNewBot(Session session) {


        botStartQueue.add(() -> {

            ++totalBotsCount;
            Bot bot = session != null ? new Bot(session) : new Bot();

            BotThreadGroup threadGroup = new BotThreadGroup(bot, "Bot " + totalBotsCount);

            bot.botIndex = totalBotsCount;
            bots.add(bot);

            Thread t = new Thread(threadGroup, () -> {
                try {

                    bot.init();
                } catch (Exception e) {

                    e.printStackTrace();
                }

            });
            t.start();

            Time.sleep(13000, () -> {
                return bot.isLoaded() || bot.crashed || !bots.contains(bot);
            });
            if (bot.crashed) {

                Time.sleep(15000);
            }

        });
        return null;
    }

    public static Process executeCommand(String commands) {

        ArrayList<String> args = new ArrayList(Arrays.asList("java", "-Xbootclasspath/p:" + Settings.XBOOT_JAR_FILE.getAbsolutePath(), "-jar", Settings.BOT_JAR_FILE.getAbsolutePath()));
        String[] arr = commands.split(" ");
        Arrays.stream(arr).forEach(args::add);
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectOutput(Redirect.INHERIT);
        pb.redirectError(Redirect.INHERIT);
        Debug.log("Starting: " + args.toString());

        try {
            return pb.start();
        } catch (IOException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static int getBotProcessIndex() {
        return ConsoleParams.contains("accountindex") ? ConsoleParams.getIntValue("accountindex") : 1;
    }

    public static String getComputerName() {
        return computerName;
    }
}
