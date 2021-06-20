package com.farm.ibot.ui;

import com.farm.ibot.Main;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.core.Bot;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class BotTabButton extends Button {
    public Bot bot;

    public BotTabButton(Bot bot) {
        this.setPrefHeight(26.0D);
        this.setOnMousePressed((arg0) -> {
            this.onClick(arg0);
        });
        this.setId("buttonTab");
        this.setBot(bot);
    }

    public void setBot(Bot bot) {
        this.bot = bot;
        Platform.runLater(() -> {
            if (bot.getSession().getAccount() != null) {
                this.setText(bot.getSession().getAccount().toString());
                Main.frame.setTitle(bot.getSession().getAccount().gameUsername);
            } else {
                this.setText(bot.getThreadGroup().getName());
                Main.frame.setTitle("RuneScape");
            }

        });
    }

    public void remove() {
    }

    public void onClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            Main.frame.switchToBot(this.bot);
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            Main.frame.showPopup(event.getSceneX(), event.getSceneY(), new MenuItem("Set account", () -> {
                this.bot.getSession().setAccount(ObjectSelector.showAccountSelector());
                this.setBot(this.bot);
            }), new MenuItem("Remove", () -> {
                Main.frame.removeBot(this.bot);
            }), new MenuItem("Login", () -> {
                Bot.currentThreadBot.proxyManager.setProxy();
                Login.login();
            }));
        }

    }

    public void updateText() {
        Platform.runLater(() -> {
            if (this.bot.getSession().getAccount() != null) {
                this.setText(this.bot.getSession().getAccount().toString());
            }

        });
    }
}
