package com.farm.ibot.plugins.onlinedatasender;

import com.farm.ibot.Main;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Terminal;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.ConsoleParams;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public class OnlineDataSender extends Plugin {
    private PaintTimer botOnlineTimer = new PaintTimer();
    private int restartTime = 360;

    public void onStart() {
        this.botOnlineTimer.reset();
    }

    private boolean isRestartingBot() {
        return !ConsoleParams.contains("dev") && !ConsoleParams.contains("norestart") && this.botOnlineTimer.getElapsedSeconds() > (long) (60 * this.restartTime);
    }

    public int onLoop() {
        ArrayList<AccountData> toUpdate = new ArrayList();
        StringBuilder onlineString = new StringBuilder();
        Iterator var3 = Main.bots.iterator();

        while (var3.hasNext()) {
            Bot bot = (Bot) var3.next();

            try {
                AccountData data = bot.getSession().getAccount();
                if (data != null) {
                    bot.executeAndWait(() -> {
                        toUpdate.add(data);
                        BotScript script = bot.getScriptHandler().getScript();
                        data.description = "";
                        data.currentScript = "None";
                        if (bot.isLoaded()) {
                            data.isLoggedIn = Client.isInGame();
                            if (data.isLoggedIn) {
                                data.isMembers = Varbit.MEMBERSHIP_DAYS.booleanValue();
                                data.world = Client.getCurrentWorld();
                                String name = (String) Optional.ofNullable(Player.getLocal()).map(Player::getName).orElse(null);
                                if (name != null) {
                                    data.gameUsername = name;
                                }
                            }

                            if (script != null) {
                                data.currentScript = script.getName();
                                if (script instanceof ScriptRuntimeInfo) {
                                    data.description = ((ScriptRuntimeInfo) script).runtimeInfo();
                                }

                                data.description = data.description + "<th>" + bot.getFpsData().getFps() + " fps";
                                onlineString.append((onlineString.length() > 0 ? ", " : "") + data.getGameUsername() + "(" + (bot.getFpsData().isOverloaded() ? "!" : "") + ")");
                            }
                        }

                    });
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

        if (toUpdate.size() > 0) {
            if (this.isRestartingBot()) {

                WebUtils.uploadObject(toUpdate, "http://api.hax0r.farm:8080/accounts/setoffline/");
                Bot.sendRestartCommand();
            } else {
                try {
                    WebUtils.uploadObject(toUpdate, "http://api.hax0r.farm:8080/accounts/update/");
                    WebUtils.downloadString("http://localhost:6666/?get=update&botid=" + Main.getBotProcessIndex() + "&pid=" + Terminal.getProcessId() + "&command=" + onlineString);
                } catch (Exception var7) {
                    var7.printStackTrace();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    var7.printStackTrace(pw);
                    String sStackTrace = sw.toString();
                    WebUtils.downloadString("http://localhost:6666/?get=update&botid=" + Main.getBotProcessIndex() + "&pid=" + Terminal.getProcessId() + "&command=" + sStackTrace);
                }
            }
        }

        return 10000;
    }
}
