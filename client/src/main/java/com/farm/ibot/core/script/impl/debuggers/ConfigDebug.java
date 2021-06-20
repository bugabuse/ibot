package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;

public class ConfigDebug extends BackgroundScript implements PaintHandler {
    int[] lastValues;

    public void onStart() {

    }

    public int onLoop() {
        int[] values = Config.getValues();
        if (this.lastValues == null) {
            this.lastValues = new int[values.length];
            System.arraycopy(values, 0, this.lastValues, 0, values.length);
        }

        for (int i = 0; i < values.length; ++i) {
            if (values[i] != this.lastValues[i]) {

                this.lastValues[i] = values[i];
            }
        }

        return 10;
    }

    public void onPaint(Graphics g) {
    }
}
