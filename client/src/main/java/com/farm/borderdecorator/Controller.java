package com.farm.borderdecorator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.swing.*;

public class Controller {
    public JFrame jFrame;

    @FXML
    public void btnClose_action(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void btnMinimize_action(ActionEvent event) {
        if (this.jFrame != null) {
            this.jFrame.setState(1);
        }

    }
}
