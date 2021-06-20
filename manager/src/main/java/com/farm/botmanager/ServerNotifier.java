package com.farm.botmanager;

public class ServerNotifier {
    public static void start() {
        new Thread(() -> {
            while (true) {
                try {
                    WebUtils.sendRequest("manager/update");
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    if (System.currentTimeMillis() - BotData.lastUpdate > 15000L) {
                        BotData.botsOnline = "";
                    }
                    Thread.sleep(6000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {

                    if (Main.isRunning && !Main.isBotActive()) {
                        try {
                            System.out.println("Restarting bot!");
                            Main.stopBot();
                            Main.isRunning = true;
                            Thread.sleep(62000L);
                            Main.startBot(Main.defaultStartCommand);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Thread.sleep(1000L);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
                break;
            }
        }).start();
    }
}

