package com.farm.ibot.ui;

import com.farm.borderdecorator.Controller;
import com.farm.ibot.Main;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.core.Bot;
import com.farm.ibot.init.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.util.Iterator;

public class BotFrameController extends Controller {
    public static FlowPane staticMenuBar;
    public static BotFrameController instance;
    @FXML
    public FlowPane menuBar;
    @FXML
    public Button start;
    @FXML
    public Button restart;
    @FXML
    public Button stop;
    @FXML
    public Button addBot;
    @FXML
    public Pane loadingPane;
    @FXML
    public Pane welcomePane;
    @FXML
    public AnchorPane clientArea;

    public BotFrameController() {


        instance = this;
        Platform.runLater(() -> {
            staticMenuBar = this.menuBar;
            this.addBot.setOnMousePressed((arg0) -> {
                this.addBot_Action(arg0);
            });
        });
    }

    public static void removeBotButton(Bot bot) {

        if (instance != null) {
            Platform.runLater(() -> {
                Iterator var1 = staticMenuBar.getChildren().iterator();

                while (var1.hasNext()) {
                    Node node = (Node) var1.next();
                    if (node instanceof BotTabButton) {
                        BotTabButton button = (BotTabButton) node;
                        if (button.bot.equals(bot)) {
                            staticMenuBar.getChildren().remove(node);
                            button.bot = null;

                            return;
                        }
                    }
                }

            });
        }

    }

    public static void addBotButton(Bot bot) {

        if (instance != null) {
            Platform.runLater(() -> {
                BotTabButton button = new BotTabButton(bot);
                if (!staticMenuBar.getChildren().contains(button)) {
                    staticMenuBar.getChildren().add(button);
                }

                instance.update();
                Debug.log("addBotButton instance.update()");
            });
        }

    }

    public static void updateButtons() {

        if (instance != null) {
            Platform.runLater(() -> {
                instance.update();
                Iterator var0 = staticMenuBar.getChildren().iterator();

                while (var0.hasNext()) {
                    Node node = (Node) var0.next();
                    if (node instanceof BotTabButton) {
                        BotTabButton btn = (BotTabButton) node;
                        btn.updateText();
                    }
                }

            });
        }

    }

    public void buttonStop_action(ActionEvent actionEvent) throws Exception {

        Bot.getSelectedBot().getScriptHandler().stop();
        updateButtons();
    }

    public void buttonStart_Action(ActionEvent actionEvent) throws Exception {

        Bot.getSelectedBot().getScriptHandler().start(Bot.getSelectedBot().getScriptHandler().scriptLoader.loadWithList());
        updateButtons();
    }

    public void buttonRestart_Action(ActionEvent actionEvent) throws Exception {

        Bot.getSelectedBot().getScriptHandler().stop();
        Bot.getSelectedBot().getScriptHandler().start(Bot.getSelectedBot().getScriptHandler().scriptLoader.loadLast());
        updateButtons();
    }

    public void addBot_Action(MouseEvent event) {

        if (event.getButton().equals(MouseButton.PRIMARY)) {
            Bot bot = Main.addNewBot((Session) null);
            Main.frame.switchToBot(bot);
            updateButtons();
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            Main.frame.showPopup(event.getSceneX(), event.getSceneY(), new MenuItem("Multiple Session Start", () -> {
                Main.addBots(ObjectSelector.showProfileSelection(), -1);
            }), new MenuItem("Manage Loggers", Debug::showDebugsDialog));
        }

    }

    public void update() {

        Bot bot = Bot.getSelectedBot();
        Platform.runLater(() -> {

            if (bot != null) {

                if (bot.getGameLoader().getApplet() != null) {

                    this.clientArea.getChildren().removeAll(new Node[]{this.loadingPane});
                    this.clientArea.getChildren().removeAll(new Node[]{this.welcomePane});
                } else {

                    this.welcomePane.setVisible(false);
                    this.loadingPane.setVisible(true);
                    if (!this.clientArea.getChildren().contains(this.welcomePane)) {

                        this.clientArea.getChildren().add(this.welcomePane);
                    }

                    if (!this.clientArea.getChildren().contains(this.loadingPane)) {

                        this.clientArea.getChildren().add(this.loadingPane);
                    }
                }

                if (bot.getScriptHandler() != null && bot.getScriptHandler().getScript() != null) {

                    this.setVisible(this.start, false);
                    this.setVisible(this.restart, false);
                    this.setVisible(this.stop, true);
                } else {

                    this.setVisible(this.start, true);
                    this.setVisible(this.restart, true);
                    this.setVisible(this.stop, false);

                }
            } else {

                this.welcomePane.setVisible(true);
                this.setVisible(this.start, false);
                this.setVisible(this.restart, false);
                this.setVisible(this.stop, false);
            }
        });
    }

    public void setVisible(Button button, boolean visible) {

        if (!visible) {
            button.setPrefWidth(0.0D);
            button.setVisible(false);
        } else {
            button.setPrefWidth(32.0D);
            button.setVisible(true);
        }

    }
}
