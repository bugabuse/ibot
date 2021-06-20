package com.farm.ibot.api.util;

import com.farm.ibot.core.Bot;
import com.farm.ibot.init.AccountData;

public class AccountUnlocker {
    private AccountData account;
    private Bot bot;

    public AccountUnlocker(Bot bot, AccountData account) {
        this.account = account;
        this.bot = bot;
    }

    public void start() {
    }
}
