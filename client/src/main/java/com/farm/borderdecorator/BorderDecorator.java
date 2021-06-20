package com.farm.borderdecorator;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;

public class BorderDecorator {
    private Stage stage;
    private JFrame frame;
    private AnchorPane root;
    private Point dragStart;

    public BorderDecorator(JFrame frame, AnchorPane root) {
        this(root);
        this.frame = frame;
    }

    public BorderDecorator(Stage stage, AnchorPane root) {
        this(root);
        this.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
    }

    private BorderDecorator(AnchorPane root) {
        this.dragStart = new Point(-1, -1);
        this.root = (AnchorPane) root.getChildren().get(0);
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handlePress);
        root.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::handleDrag);
        root.addEventFilter(MouseEvent.MOUSE_RELEASED, this::handleRelease);
    }

    public void setBorderStyle(String style) {
        this.root.getScene().getStylesheets().add(style);
    }

    private void handleRelease(MouseEvent event) {
        this.dragStart = new Point(-1, -1);
    }

    private void handlePress(MouseEvent event) {
        if (this.isNonClientArea(event)) {
            this.dragStart = new Point((int) event.getX(), (int) event.getY());
            event.consume();
        }

    }

    private void handleDrag(MouseEvent event) {
        if (this.dragStart.getY() > -1.0D && this.dragStart.getX() > -1.0D) {
            this.setPosition(event.getScreenX() - this.dragStart.getX(), event.getScreenY() - this.dragStart.getY());
            event.consume();
        }

    }

    private boolean isNonClientArea(MouseEvent event) {
        return event.getTarget().equals(this.root);
    }

    public void setPosition(double x, double y) {
        if (this.stage != null) {
            this.stage.setX(x);
            this.stage.setY(y);
        } else if (this.frame != null) {
            this.frame.setLocation((int) x, (int) y);
        }

    }
}
