package com.farm.ibot.api.util.web.account;

import com.farm.ibot.api.util.WebUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class AccountConfig extends HashMap<String, Object> {
    private transient String loginEmail;

    public AccountConfig() {
    }

    public AccountConfig(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public static AccountConfig fetch(String loginEmail) {
        try {
            AccountConfig config = (AccountConfig) (new ObjectMapper()).enable(DeserializationFeature.USE_LONG_FOR_INTS).readValue(WebUtils.download("account/config/get/" + loginEmail + "/"), AccountConfig.class);
            config.loginEmail = loginEmail;
            return config;
        } catch (IOException var2) {
            var2.printStackTrace();
            return new AccountConfig(loginEmail);
        }
    }

    public void update() {
        WebUtils.uploadObject(this, "account/config/set/" + this.loginEmail + "/");
    }

    public long getLong(String key) {
        return ((Number) this.get(key)).longValue();
    }

    public int getInt(String key) {
        return ((Number) this.get(key)).intValue();
    }
}
