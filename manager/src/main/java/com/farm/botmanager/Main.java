package com.farm.botmanager;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static final BlockingQueue<Runnable> threadQueue = new LinkedBlockingQueue<Runnable>();
    public static String pid = "";
    public static String defaultStartCommand = "";
    public static String additionalStartCommand = "";
    public static boolean isRunning = false;
    public static String START_MEMORY = "-Xmx1024m";
    private static File file = new File("properties.dat");
    private static Properties properties = new Properties();
    private static Process botProcess = null;

    public static void main(String[] args) throws IOException {
    	System.setProperty("java.net.preferIPv4Stack", "true");

        ServerNotifier.start();
        WebServer.start();
        Main.loadProperties();
        Settings.load();
        defaultStartCommand = properties.getProperty("startCommand");
        while (true) {
            try {
                threadQueue.take().run();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
    }

    public static void runOnMainThread(Runnable r) {
        threadQueue.add(r);
    }

    public static void onCommand(String command) {
        System.out.println("Threads in queue: " + threadQueue.size());
        Main.runOnMainThread(() -> {
            System.out.println(Thread.currentThread());
            System.out.println("Command: " + command);
            if (command.startsWith("start ")) {
                Main.startBot(command.substring("start ".length()));
            }
            if (command.startsWith("stop")) {
                Main.stopBot();
            }
            if (command.startsWith("restart_bot")) {
                Main.stopBot();
                Main.startBot(defaultStartCommand);
            }
            if (command.startsWith("/")) {
                Main.sendCommand(command.substring(1));
            }
        });
    }

    private static void sendCommand(String command) {
        if (Main.isBotActive()) {
            OutputStream rsyncStdIn = botProcess.getOutputStream();
            try {
                rsyncStdIn.write(command.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startBot(String startCommands) {
        if (!Main.isBotActive()) {
            isRunning = true;
            botProcess = Terminal.execute(Settings.BOT_JAR_FILE.getAbsolutePath(), startCommands + " " + additionalStartCommand);
            if (!startCommands.contains("undefined")) {
                defaultStartCommand = startCommands;
                additionalStartCommand = "";
                properties.setProperty("startCommand", defaultStartCommand);
                Main.saveProperties();
            }
            START_MEMORY = startCommands.contains("Xmx") ? "-Xmx" + startCommands.split("Xmx")[1].split(" ")[0] : "-Xmx1024m";
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.startListening(botProcess.getErrorStream());
            Main.startListening(botProcess.getInputStream());
            if (!botProcess.isAlive()) {
                System.out.println("Re-downloading bot-client");
                File file = new File(Settings.BOT_JAR_FILE.getAbsolutePath());
                try {
                    FileUtils.copyURLToFile(new URL("http://api.hax0r.farm:8080/download/osrsbot.jar"), file);
                    System.out.println("Bot downloaded.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void startListening(InputStream stream) {
        ThreadGroup tg = new ThreadGroup("Listener");
        new Thread(tg, () -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder(1024);
            try {
                int read;
                new Thread(tg, () -> {
                    long lastUpdate = System.currentTimeMillis();

                    try {
                        if (builder.toString().contains("ClassNotFoundException")) {
                            Main.stopBot();
                            isRunning = true;
                        }
                        if (builder.toString().contains("in thread \"main\"")) {
                            System.out.println("Exception in main thread. Restarting bot.");
                            Main.stopBot();
                            isRunning = true;
                        }
                        if (builder.toString().equalsIgnoreCase("restart_bot")) {
                            Main.stopBot();
                            isRunning = true;
                        }
                        if (builder.toString().contains("restart_bot_ban")) {
                            Main.stopBot();
                            isRunning = true;
                            additionalStartCommand = "-ban";
                        }
                        if (builder.toString().contains("Exception: gameImage")) {
                            System.out.println("Started without xboot. Restarting bot.");
                            Main.stopBot();
                            isRunning = true;
                        }
                        if (builder.toString().contains("OutOfMemory")) {
                            System.out.println("OutOfMemory exception - restarting bot.");
                            Main.stopBot();
                            isRunning = true;
                        }
                        if (builder.length() > 512 || System.currentTimeMillis() - lastUpdate > 100L) {
                            System.out.print(builder.toString());
                            builder.delete(0, builder.length());
                            lastUpdate = System.currentTimeMillis();
                        }
                        Thread.sleep(100L);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }).start();
                while ((read = reader.read()) != -1) {
                    builder.append((char) read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static void stopBot() {
        isRunning = false;
        Main.killBotProcess();
    }

    public static void killBotProcess() {
        try {
            if (Main.isBotActive()) {
                botProcess.destroy();
            }
            System.out.println("Killing bot process: " + pid);
            Terminal.killProcess(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean isBotActive() {
        return botProcess != null && botProcess.isAlive();
    }

    public static BotData getData() {
        BotData data = new BotData();
        data.botActive = Main.isBotActive();
        data.defaultStartData = defaultStartCommand;
        data.hostName = BotData.botsOnline;
        data.cpuData = CpuInfo.getData();
        return data;
    }

    private static void saveProperties() {
        try {
            FileOutputStream fr = new FileOutputStream(file);
            properties.store(fr, "Properties");
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadProperties() throws IOException {
        try {
            if (file.exists()) {
                FileInputStream fi = new FileInputStream(file);
                properties.load(fi);
                fi.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getHostName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
            return "Untnown";
        }
    }
}

