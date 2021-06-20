package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Camera;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.core.script.BackgroundScript;
import com.farm.ibot.core.script.impl.random.LoginRandom;

import java.awt.*;
import java.util.Arrays;

public class ClientDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Game state" + Client.getLoginState());
        this.drawString(g, "Item selected: " + Client.getSelectedItemId());
        this.drawString(g, "Dialogue: " + Dialogue.isInDialouge());
        this.drawString(g, "Dialogue con: " + Dialogue.canClickContinue());
        this.drawString(g, "Dialogue: " + Client.getLoginState());
        this.drawString(g, "Camera pitch: " + Client.getLastAction());
        this.drawString(g, "Camera pitch: " + Camera.getPitch());
        this.drawString(g, "Camera yaw: " + Camera.getYaw());
        this.drawString(g, "Camera x: " + Camera.getX());
        this.drawString(g, "Camera y: " + Camera.getY());
        this.drawString(g, "Camera z: " + Camera.getZ());
        this.drawString(g, "Camera getZoomExact: " + Client.getZoomExact());
        this.drawString(g, "Resizable " + Client.getGameConfig().getResizableMode());
        this.drawString(g, "Login message: " + Client.getLoginMessage());
        this.drawString(g, "Login screen state: " + Login.getState());
        this.drawString(g, "Account state " + Client.getLoginAccountState());
        this.drawString(g, "Login state: " + Client.getLoginState());
        this.drawString(g, "Login screen id: " + Client.getLoginScreenId());
        this.drawString(g, "Bank open " + Bank.isOpen());
        this.drawString(g, "Run energy " + Client.getRunEnergy());
        this.drawString(g, "Continue:  " + Dialogue.canClickContinue());
        this.drawString(g, "Input box open:  " + InputBox.isOpen());
        this.drawString(g, "Must click Continue:  " + Dialogue.mustClickContinue());
        this.drawString(g, "Mem days  " + Varbit.MEMBERSHIP_DAYS.intValue());
        this.drawString(g, "Mem?  " + Varbit.MEMBERSHIP_DAYS.booleanValue());
        this.drawString(g, "Withdraw amount  " + Varbit.WITHDRAW_X_AMOUNT.intValue() / 2);
        this.drawString(g, "Logged out  " + LoginRandom.isLoggedOut());
        this.drawString(g, "Has house  " + Varbit.HAS_HOUSE.intValue());
        this.drawString(g, "F2P:  " + Arrays.toString(WorldHopping.F2P_WORLDS));
        this.drawString(g, "P2P:  " + Arrays.toString(WorldHopping.P2P_WORLDS));
    }
}
