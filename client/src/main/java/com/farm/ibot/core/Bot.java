// Decompiled with: FernFlower
package com.farm.ibot.core;

import com.farm.ibot.Main;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.GameShell;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.applet.GameLoader;
import com.farm.ibot.core.canvas.CanvasHandler;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.ScriptHandler;
import com.farm.ibot.core.script.impl.debuggers.UsageDebug;
import com.farm.ibot.core.util.StealthBotUtils;
import com.farm.ibot.init.ConsoleParams;
import com.farm.ibot.init.Session;
import com.farm.ibot.proxy.Proxy;
import com.farm.ibot.proxy.ProxyManager;
import com.farm.ibot.ui.BotFrameController;
import javafx.application.Platform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bot {
    public static Bot currentThreadBot = null;
    public AccessorInterface accessorInterface;
    public int worldHopInterfaceFails;
    public StealthBotUtils.SpoofedOperatingSystem spoofedOperatingSystem = StealthBotUtils.SpoofedOperatingSystem.createRandom();
    public boolean crashed = false;
    public FpsData fpsData = new FpsData();
    public PaintTimer loadedTimer = new PaintTimer();
    public HashMap properties = new HashMap();
    public int botIndex = 0;
    public Proxy proxy;
    public ProxyManager proxyManager;
    private ScriptHandler scriptHandler;
    private GameLoader gameLoader = new GameLoader(this);
    private CanvasHandler canvasHandler = new CanvasHandler(this);
    private Mouse mouse;
    private Keyboard keyboard;
    private BotThreadGroup threadGroup;
    private boolean loaded;
    private Session session;
    private BufferedImage gameImage;
    private int lastCanvasHash;
    private ExecutorService executor;

    public Bot(Session session) {
        this.session = session;
        this.accessorInterface = new AccessorInterface();
        this.proxyManager = new ProxyManager(this);
    }

    public Bot() {
        this.session = new Session();
        this.accessorInterface = new AccessorInterface();
        this.proxyManager = new ProxyManager(this);
    }

    public static Bot getSelectedBot() {
        return Main.frame.selectedBot;
    }

    public static Bot get() {
        ThreadGroup parent = Thread.currentThread().getThreadGroup();

        try {
            return ((BotThreadGroup) parent).getBot();
        } catch (Exception var2) {
            return currentThreadBot;
        }
    }

    public static void runLater(Runnable runnable) {
        Platform.runLater(() -> {
            (new Thread(runnable)).start();
        });
    }

    public static void sendRestartAsBanCommand() {

    }

    public static void sendRestartCommand() {
        if (ConsoleParams.contains("restart_old")) {
            System.exit(0);
        } else {

        }

    }

    public void execute(Runnable r) {
        this.executor.execute(r);
    }

    public void executeAndWait(Runnable r) {
        AtomicBoolean executed = new AtomicBoolean(false);
        this.executor.execute(() -> {
            try {
                r.run();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            executed.set(true);
        });

        while (!executed.get()) {
            try {
                Thread.sleep(0L, 100000);
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }
        }

    }

    public BotThreadGroup getThreadGroup() {
        return this.threadGroup;
    }

    public void reload(boolean clearCache) {
        runLater(() -> {
            Session session = this.getSession();
            this.remove();
            Time.sleep(10000);
            runLater(() -> {
                if (clearCache) {
                    this.getGameLoader().clearCache();
                }

                Time.sleep(30000);
                Main.addNewBot(session);
            });
        });
    }

    public void reload() {
        this.reload(false);
    }

    public void init() {

        this.threadGroup = (BotThreadGroup) Thread.currentThread().getThreadGroup();
        this.executor = Executors.newFixedThreadPool(12);
        this.scriptHandler = new ScriptHandler(this);
        this.gameLoader.load();
        System.setProperty("consumeClickEvent", "0");
        Main.frame.addBot(this);
        BotFrameController.addBotButton(this);
        this.loadedTimer.reset();
        if (this.waitForGameInitialization()) {
            this.onGameInitialized();
        }

        this.listenGameClientCrash();
    }

    private void listenGameClientCrash() {
        while (Main.bots.contains(this)) {
            try {
                this.updateCanvas();
            } catch (Exception var2) {
            }

            if (this.crashed) {
                this.crashed = false;
                System.out.println("Reloading because of crash.");
                this.reload();
                return;
            }

            Time.sleep(50);
        }

    }

    public boolean waitForGameInitialization() {
        while (Client.getLoginState() < 10) {
            if (this.loadedTimer.getElapsedSeconds() > 240L) {
                this.gameLoader.onGameCrash();
                return false;
            }

            if (this.crashed) {
                return false;
            }

            Time.sleep(10);
        }

        return true;
    }

    public void updateCanvas() {
        if (!Objects.equals(this.lastCanvasHash, GameShell.getInstance().getCanvas().hashCode()) || this.gameImage == null) {
            this.injectCanvas();
        }
    }

    public void injectCanvas() {
        try {
            Debug.log("Injecting canvas.");
            final Canvas canvas = GameShell.getInstance().getCanvas();
            this.lastCanvasHash = canvas.hashCode();
            Mouse.getMouse().addListeners();
            this.gameImage = (BufferedImage) canvas.getClass().getField("gameImage").get(canvas);
            if (canvas != null) {

                PaintHandler ph = (g) -> {
                    this.canvasHandler.paint(g);
                };
                canvas.getClass().getField("paintHandler").set(canvas, ph);
            } else {
                Debug.log("Game canvas is null.");
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void onGameInitialized() {
        this.onGameInitialized(true);
    }

    private void loadPlugins() {
    }

    public void onGameInitialized(boolean autostart) {
        this.loadPlugins();
        this.mouse = new Mouse();
        this.keyboard = new Keyboard();
        this.injectCanvas();

        this.proxyManager.setProxy();
        this.getScriptHandler().addDebug(UsageDebug.class);
        if (autostart && this.session.getAccount() != null && this.session.autostartScript != null && this.session.getAccount().autostartScript.length() > 1) {
            (new Thread(this.getThreadGroup(), () -> {
                try {
                    BotScript script = this.scriptHandler.getScriptLoader().load(this.session.autostartScript);
                    this.scriptHandler.start(script);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            })).start();
        }

        this.loaded = true;
    }

    public ScriptHandler getScriptHandler() {
        return this.scriptHandler;
    }

    public GameLoader getGameLoader() {
        return this.gameLoader;
    }

    public CanvasHandler getCanvasHandler() {
        return this.canvasHandler;
    }

    public Mouse getMouse() {
        return this.mouse;
    }

    public Keyboard getKeyboard() {
        return this.keyboard;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public Session getSession() {
        return this.session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public BufferedImage getGameImage() {
        this.getCanvasHandler().enableInput();
        return this.gameImage;
    }

    public FpsData getFpsData() {
        return this.fpsData;
    }

    public void destroy() {
        try {
            currentThreadBot = null;
            this.canvasHandler = null;
            this.getThreadGroup().interrupt();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void remove() {
        runLater(() -> {
            Main.frame.removeBot(this);
        });
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public String getFullHostName() {
        return this.proxy != null ? this.proxy.getName() : Main.getComputerName();
    }

    public void clearCachedObjects() {

        Bank.cache.remove(this);
        Widget.widgetsCache.remove(this);
    }
}
