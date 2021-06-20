package com.farm.ibot.core.script.impl.random;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.listener.LoginMessageEventHandler;
import com.farm.ibot.api.listener.LoginMessageListener;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.KeyBindingConfig;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.RandomEvent;
import com.farm.ibot.core.script.manifest.ScriptManifest;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.ConsoleParams;
import com.farm.ibot.plugins.proxydatasender.ProxyRecord;
import com.farm.ibot.proxy.Proxy;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import java.awt.*;

@ScriptManifest(
        isP2p = true
)
public class LoginRandom extends RandomEvent implements PaintHandler, LoginMessageListener {
    public boolean autoWorldAssignEnabled = true;
    LoginMessageEventHandler eventHandler = new LoginMessageEventHandler(this);
    int bestWorld = -1;
    private PaintTimer lastTryTimer = new PaintTimer(0L);
    private int invalidPasswordCount = 0;
    private int loggedInAttempts = 0;
    private PaintTimer lastWorldChangeTimer = new PaintTimer(0L);
    private PaintTimer connectingTimer = new PaintTimer(0L);
    private boolean isMembersOnly;
    private boolean waitingToLogin = false;
    private String waitingToLoginMessage = "";

    public LoginRandom() {
        this.addEventHandler(this.eventHandler);
    }

    public static boolean isLoggedOut() {
        Widget widget = Widgets.get(378, 87);
        return Client.getLoginState() != 25 && Client.getLoginState() != 30 || widget != null && widget.isRendered();
    }

    public static void switchProxy() {
        if (ConsoleParams.contains("dynamicProxy")) {

            ProxyRecord record = (ProxyRecord) WebUtils.downloadObject(ProxyRecord.class, "http://api.hax0r.farm:8080/proxy/assign");
            if (record != null) {
                if (AccountData.current() != null) {
                    AccountData.current().proxy = record.proxy;
                }

                Proxy proxy = new Proxy(record.proxy);
                proxy.sessionId = record.sessionId;
                Bot.get().proxy = proxy;
            }
        }

    }

    public void onPaint(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(10, drawStringY - 20, 170, 80);
        g.setColor(Color.white);
        this.drawString(g, "Login random event");
        this.drawString(g, "Login state: " + Client.getLoginState());
        if (Client.getLoginState() == 20) {
            this.drawString(g, "Connecting time: " + this.connectingTimer.getElapsedSeconds());
        }

        if (this.waitingToLogin) {
            this.drawString(g, this.waitingToLoginMessage);
        }

    }

    public void onStart() {

        this.bestWorld = -1;
        this.lastWorldChangeTimer = new PaintTimer(0L);
    }

    public int onLoop() {
        if (this.connectingTimer.getElapsedSeconds() > 45L && Client.getLoginState() == 20) {

            return 1000;
        } else if (this.waitingToLogin) {
            return 5000;
        } else if (Client.getLoginState() == 1000 && !Time.sleep(240000, () -> {
            return Client.getLoginState() != 1000;
        })) {
            Bot.get().getGameLoader().onGameCrash();
            return 1000;
        } else {
            BotScript script = Bot.get().getScriptHandler().getMainScript();
            Widget playButton = Widgets.get((w) -> {
                return w.getTextureId() == 429 && w.isVisible() && w.isRendered();
            });
            if (playButton != null) {
                if (playButton.interact("") && Time.sleep(() -> {
                    return Widgets.get((w) -> {
                        return w.getTextureId() == 429 && w.isRendered();
                    }) == null;
                })) {
                    Time.sleep(600, 900);
                }

                return 500;
            } else {
                try {

                    Debug.log(Widgets.get((w) -> {
                        return w.getTextureId() == 429;
                    }).getLoopCycle());
                    Debug.log(Widgets.get((w) -> {
                        return w.getTextureId() == 429;
                    }).isVisible());
                    Debug.log(Widgets.get((w) -> {
                        return w.getTextureId() == 429;
                    }).isRendered());
                } catch (Exception var5) {
                }

                if (this.autoWorldAssignEnabled && !Client.isInGame()) {
                    if (this.lastWorldChangeTimer.getElapsedSeconds() > 60L || this.bestWorld == -1) {
                        this.bestWorld = this.findBestWorldForAccount();
                        this.lastWorldChangeTimer.reset();
                        if (Bot.get().getSession().getAccount() != null) {
                            Bot.get().getSession().getAccount().world = this.bestWorld;
                        }
                    }

                    if (Client.getWorlds((w) -> {
                        return WorldHopping.equals(w.getId(), this.bestWorld);
                    }).size() == 0) {
                        if (this.isMembersOnly) {
                            this.bestWorld = WorldHopping.getRandomP2p();
                        } else {
                            this.bestWorld = WorldHopping.getRandomF2p();
                        }
                    }


                    if (!WorldHopping.hop(this.bestWorld)) {
                        return 1000;
                    }

                    if (!WorldHopping.isF2p(Client.getCurrentWorld()) && !script.getScriptManifest().isP2p() && !this.isMembersOnly && !WorldHopping.hop(WorldHopping.getRandomF2p())) {
                        return 1000;
                    }
                } else if (this.isMembersOnly && WorldHopping.isF2p(Client.getCurrentWorld())) {
                    int p2pWorld = WorldHopping.getRandomP2p();

                    for (int i = 0; i < 5 && !WorldHopping.hop(p2pWorld); ++i) {
                    }
                }

                if (Client.getLoginScreenId() != 4) {
                    if (Client.getLoginState() != 30) {
                        if (this.lastTryTimer.getElapsed() > 9000L) {
                            this.eventHandler.lastAccountLoginState = -1;
                            Client.setLoginAccountState(-1);
                            Bot.get().getSession().getAccount().isFlaggedAsStolen = false;
                            Login.login(Bot.get().getSession().getAccount());
                            this.lastTryTimer.reset();
                        }
                    } else {
                        this.invalidPasswordCount = 0;
                        this.loggedInAttempts = 0;
                    }

                    return 500;
                } else {
                    this.invalidPasswordCount = 0;
                    Mouse.click(733, 13);
                    Time.sleep(1000);
                    if (Bot.get().getSession().getAccount().twoFactorSecretKey != null && Bot.get().getSession().getAccount().twoFactorSecretKey.length() > 5) {
                        String pin = String.format("%06d", (new GoogleAuthenticator()).getTotpPassword(Bot.get().getSession().getAccount().twoFactorSecretKey));

                        Client.setTwoFactorPin(pin);
                        Mouse.click(292, 318);
                    } else {

                        Bot.get().getSession().getAccount().isBanned = true;
                    }

                    return 1000;
                }
            }
        }
    }

    public boolean isEnabled() {
        KeyBindingConfig.setDefaults();
        Bot.get().getGameLoader().world = Client.getCurrentWorld();
        return isLoggedOut();
    }

    public boolean isBackground() {
        return false;
    }

    public void onLoginMessage(String message, int loginState) {

        this.connectingTimer.reset();
        if (Client.getLoginScreenId() == 4) {
            this.invalidPasswordCount = 0;
        }

        if (Client.getLoginMessage().contains("timed out")) {
            switchProxy();
        }

        if (Client.getLoginState() == 10 && Client.getLoginScreenId() == 2 && Client.getLoginMessage().contains("Too many login attempts")) {

            this.waitingToLogin = true;
            this.waitingToLoginMessage = "Too many login attempts.";
            Time.sleep(10000);
            this.waitingToLogin = false;
            switchProxy();
        }

        if (Client.getLoginScreenId() == 12) {
            if (Client.getLoginAccountState() == 0) {
                Bot.get().getSession().getAccount().isBanned = true;
                Debug.log("Our account is disabled: " + Bot.get().getSession().getAccount().username);
            } else if (Client.getLoginAccountState() == 1) {
                AccountData data = Bot.get().getSession().getAccount();
                Bot.get().getSession().getAccount().isFlaggedAsStolen = true;
                this.waitingToLogin = true;
                this.waitingToLoginMessage = "Waiting for account unlock.";
                Time.sleep(45000);
                this.waitingToLogin = false;
                return;
            }
        }

        if (message.contains("display")) {
            Bot.get().getSession().getAccount().isBanned = true;
            Debug.log("Display name thing. " + Bot.get().getSession().getAccount().username);
        }

        if (Client.getLoginScreenId() == 3) {
            Debug.log("Invalid password - " + Bot.get().getSession().getAccount().username);
            this.lastTryTimer.reset();
            ++this.invalidPasswordCount;
            if (this.invalidPasswordCount > 4 && ("" + AccountData.current().twoFactorSecretKey).length() < 6) {

                Bot.get().getSession().getAccount().isBanned = true;
            }
        }

        if (message.contains("members-only area")) {

            this.isMembersOnly = true;
            this.bestWorld = -1;
        }

        if (message.contains("already logged in") || message.contains("not logged out")) {
            ++this.loggedInAttempts;
            if (this.loggedInAttempts > 5) {
                Bot.get().getSession().setAccount((AccountData) null);
                Bot.get().getScriptHandler().stop();
            }
        }

        if (message.contains("members account")) {
            this.isMembersOnly = false;
            this.bestWorld = -1;
        }

        if (message.contains("Error connecting to server")) {
            ++Bot.get().worldHopInterfaceFails;
            if (ConsoleParams.contains("dynamicProxy")) {

                switchProxy();
            }
        }

        if (message.contains("many connections")) {
            int world = WorldHopping.getRandomF2p();

            for (int i = 0; i < 5 && !WorldHopping.hop(world); ++i) {
            }
        }

    }

    private int findBestWorldForAccount() {
        if (!AccountData.current().isMembers && !this.isMembersOnly) {
            return WorldHopping.isF2p(Client.getCurrentWorld()) ? Client.getCurrentWorld() : WorldHopping.getRandomF2p();
        } else {
            return WorldHopping.getRandomP2p();
        }
    }
}
