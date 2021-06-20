package com.farm.ibot.api.listener;

import com.farm.ibot.api.accessors.Client;

public class LoginMessageEventHandler extends EventHandler {
    public int lastAccountLoginState = -1;
    private LoginMessageListener listener;
    private int lastLoginState = 0;

    public LoginMessageEventHandler(LoginMessageListener listener) {
        this.listener = listener;
    }

    public int listen() {
        if (Client.getLoginMessage().contains("Enter your username")) {
            this.lastAccountLoginState = -1;
            this.lastLoginState = -1;
            Client.setLoginAccountState(-1);
        }

        if (this.lastAccountLoginState == Client.getLoginAccountState() && this.lastLoginState == Client.getLoginState()) {
            return 100;
        } else {
            this.lastLoginState = Client.getLoginState();
            this.lastAccountLoginState = Client.getLoginAccountState();
            this.listener.onLoginMessage(Client.getLoginMessage(), this.lastLoginState);
            if (Client.getLoginScreenId() != 4 && Client.getLoginScreenId() != 3) {
                Client.setLoginAccountState(-1);
            }

            return 100;
        }
    }
}
