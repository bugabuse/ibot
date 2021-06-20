package com.farm.ibot.core.applet;

import com.farm.ibot.Main;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.reflection.classloader.GameClassLoader;
import com.farm.ibot.core.reflection.classloader.GameInjectionClassLoader;
import com.farm.ibot.init.Settings;
import org.apache.commons.io.FileUtils;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;

public class GameLoader {
    private static final String USER_HOME = System.getProperty("user.home");
    public int world = -1;
    private Applet applet;
    private GameClassLoader classLoader;
    private Bot bot;
    private String cacheFolderName;

    public GameLoader(Bot bot) {
        this.bot = bot;
    }

    public void load() {
        try {
            this.world = 308;
            JagexConfiguration config = JagexConfiguration.fetch(this.world);

            while (config == null) {
                this.world = WorldHopping.getRandomF2p();

                config = JagexConfiguration.fetch(this.world);
                Time.sleep(10);
            }

            URL baseUrl = new URL("http://oldschool" + this.world + ".runescape.com");
            URL loadUrl = new URL("http://oldschool" + this.world + ".runescape.com/gamepack.jar");
            if (!Settings.GAMEPACK_FILE.exists()) {
                Debug.log("Downloading gamepack: " + Settings.GAMEPACK_FILE.getAbsolutePath());
                FileUtils.copyURLToFile(loadUrl, Settings.GAMEPACK_FILE);

            }

            this.cacheFolderName = "cache_" + Main.getBotProcessIndex() + "_" + this.bot.botIndex;
            this.clearCache();
            String cacheLocation = USER_HOME + File.separator + "gamecache" + File.separator + this.cacheFolderName;
            this.bot.properties.put("user.home", cacheLocation);
            PrintStream myStream = new PrintStream(System.out) {
                public void println(String x) {
                    if (!x.contains("client.java")) {
                        super.println(x);
                        if (x.contains("error_game_")) {
                            GameLoader.this.onGameCrash();
                        }

                    }
                }
            };
            System.setOut(myStream);
            this.classLoader = new GameInjectionClassLoader(Settings.GAMEPACK_FILE.getAbsolutePath());
            this.applet = (Applet) this.classLoader.loadClass("client").newInstance();
            this.applet.setStub(new BotStub(this.applet, baseUrl, (HashMap) config.get("param")));
            this.applet.setPreferredSize(new Dimension(765, 503));
            this.applet.setBounds(0, 0, 765, 503);
            this.applet.setVisible(true);
            this.applet.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int findBestWorld() {
        return WorldHopping.getRandomF2p();
    }

    public GameClassLoader getClassLoader() {
        return this.classLoader;
    }

    public void destroy() {
        this.classLoader.unload();
        this.bot = null;
        this.applet.setStub((AppletStub) null);
        this.applet.setVisible(false);
        this.applet.stop();
        this.applet.destroy();
        this.applet = null;
    }

    public Applet getApplet() {
        return this.applet;
    }

    public void clearCache() {
        Debug.log("Clearing cache files: " + (new File(USER_HOME + File.separator + "gamecache" + File.separator + this.cacheFolderName)).getAbsolutePath());
        try {
            (new File((new File(USER_HOME + File.separator + "gamecache" + File.separator + this.cacheFolderName + "jagex_cl_oldschool_LIVE.dat")).getAbsolutePath())).delete();
            (new File((new File(USER_HOME + File.separator + "gamecache" + File.separator + this.cacheFolderName + "random.dat")).getAbsolutePath())).delete();
        } catch (Exception e) {

        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void onGameCrash() {
        if (this.bot != null) {
            Debug.log("Detected game crash! Reloading client: " + Thread.currentThread().getThreadGroup().getName());
            if (Main.crashCount < 15) {
                this.bot.crashed = true;
                ++Main.crashCount;
            }
        }

    }
}
