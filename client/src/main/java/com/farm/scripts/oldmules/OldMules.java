package com.farm.scripts.oldmules;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.Session;

import java.awt.*;

public class OldMules extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();

    public OldMules() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {
        int i = 0;
        AccountData[] var2 = Session.getAccountsCache();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            AccountData data = var2[var4];
            if (i < 200 && !data.isBanned) {
                Debug.log(data.getGameUsername());
                AccountData var6 = (AccountData) WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + data.username + "&script=Manual");
            }

            ++i;
        }


        Strategies.init(this);
        Bot.get().getScriptHandler().loginRandom.autoWorldAssignEnabled = false;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.03");
        this.drawString(g, "Strategy: " + this.getCurrentlyExecuting());
        if (Strategies.muleManager != null && Strategies.muleManager.toWithdraw != null) {
            this.drawString(g, "");
            this.drawString(g, "Wealth: " + Strategies.muleManager.toWithdraw.calculateWealth());
            this.drawString(g, "Trading items:");
            WithdrawItem[] var2 = Strategies.muleManager.toWithdraw.getPriceableItemArray();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Item item = var2[var4];
                this.drawString(g, item.getName() + "(" + item.getId() + "): " + item.getAmount());
            }
        }

    }

    public String runtimeInfo() {
        return "<th>Mules</th>";
    }
}
