package com.farm.ibot.init;

import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.WebUtils;

public class Session {
    private static AccountData[] accounts;
    private static PaintTimer fetchTimer = new PaintTimer(0L);
    public String autostartScript;
    private AccountData currentAccount;

    public Session() {
    }

    public Session(AccountData account, String autostartScript) {
        this.currentAccount = account;
        this.autostartScript = autostartScript;
    }

    public Session(AccountData account) {
        this.currentAccount = account;
    }

    public static AccountData[] fetchAccounts() {
        accounts = (AccountData[]) WebUtils.downloadObject(AccountData[].class, "http://api.hax0r.farm:8080/accounts/accounts");
        fetchTimer.reset();
        return accounts;
    }

    public static AccountData[] getAccountsCache() {
        return accounts != null && fetchTimer.getElapsed() <= 60000L ? accounts : fetchAccounts();
    }

    public static void load() {
    }

    public AccountData getAccount() {
        return this.currentAccount;
    }

    public void setAccount(AccountData account) {
        this.currentAccount = account;
    }
}
