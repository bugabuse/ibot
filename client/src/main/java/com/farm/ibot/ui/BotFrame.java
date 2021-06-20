// Decompiled with: FernFlower
package com.farm.ibot.ui;

import com.farm.borderdecorator.BorderDecorator;
import com.farm.borderdecorator.Controller;
import com.farm.ibot.Main;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.Bot;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.io.IOException;

public class BotFrame extends JFrame {
    public Bot selectedBot;
    JFXPanel fxPanel = new JFXPanel();
    JPanel panel = new JPanel();
    JPopupMenu popupMenu = new JPopupMenu();
    AnchorPane root;

    public void load() {

        boolean[] loaded = new boolean[]{false};
        Platform.runLater(() -> {
            try {
                this.initFX(this, this.fxPanel);
                loaded[0] = true;
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        });

        while (!loaded[0]) {
            Time.sleep(100);
        }


        this.setUndecorated(true);
        this.setTitle("RuneScape");
        this.panel.setLayout((LayoutManager) null);
        this.panel.add(this.fxPanel);
        this.setContentPane(this.panel);
        this.setVisible(true);
        this.setDefaultCloseOperation(3);


    }

    public void showPopup(double x, double y, MenuItem... items) {
        this.popupMenu.removeAll();
        MenuItem[] var6 = items;
        int var7 = items.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            MenuItem item = var6[var8];
            JMenuItem menuItem = new JMenuItem(item.text);
            menuItem.addActionListener((e) -> {
                item.actionEvent.onClick();
            });
            this.popupMenu.add(menuItem);
        }

        this.popupMenu.show(this, (int) x, (int) y);
    }

    public void switchToBot(Bot bot) {
        this.selectedBot = bot;
        Bot.currentThreadBot = bot;
        if (bot != null && bot.getGameLoader().getApplet() != null) {
            bot.getGameLoader().getApplet().setVisible(true);
        }

        Component[] var2 = this.panel.getComponents();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Component component = var2[var4];
            if (component instanceof Applet && (bot == null || !component.equals(bot.getGameLoader().getApplet()))) {
                component.setVisible(false);
            }
        }

        BotFrameController.updateButtons();
    }

    public void removeBot(Bot bot) {
        (new Thread(() -> {
            Main.bots.remove(bot);
            bot.getScriptHandler().stop();
            this.panel.remove(bot.getGameLoader().getApplet());
            bot.getGameLoader().destroy();
            bot.destroy();
            this.revalidate();
            if (Main.frame.selectedBot == bot) {
                Main.frame.selectedBot = null;
            }

            BotFrameController.removeBotButton(bot);
            BotFrameController.updateButtons();
            Time.sleep(3000);
            System.gc();
        })).start();
    }

    public void addBot(Bot bot) {

        (new Thread(() -> {
            Applet applet = bot.getGameLoader().getApplet();
            AnchorPane clientArea = (AnchorPane) this.root.getChildren().get(1);
            applet.setBounds((int) clientArea.getLayoutX() + 1, (int) clientArea.getLayoutY(), 765, 503);
            this.panel.add(applet, 0);
            if (this.selectedBot == null || this.selectedBot.equals(bot)) {
                this.switchToBot(bot);
            }

            BotFrameController.updateButtons();
            this.revalidate();
        })).start();
    }

    private void initFX(JFrame frame, JFXPanel fxPanel) throws IOException {

        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("main-frame.fxml"));
        this.root = (AnchorPane) loader.load();
        ((Controller) loader.getController()).jFrame = frame;
        fxPanel.setScene(new Scene(this.root, this.root.getPrefWidth(), this.root.getPrefHeight()));
        frame.setBounds(0, 0, (int) this.root.getPrefWidth(), (int) this.root.getPrefHeight());
        fxPanel.setBounds(0, 0, (int) this.root.getPrefWidth(), (int) this.root.getPrefHeight());
        BorderDecorator decorator = new BorderDecorator(frame, this.root);

        decorator.setBorderStyle("border-style.css");
    }
}
