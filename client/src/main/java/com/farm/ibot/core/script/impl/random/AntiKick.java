package com.farm.ibot.core.script.impl.random;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.input.KeyBindingConfig;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.RandomEvent;

public class AntiKick extends RandomEvent {
    public void onStart() {
    }

    public int onLoop() {
        if (Player.getLocal().getAnimation() != -1) {

            Mouse.moveOffScreen();
            Time.sleep(200, 5000);
            Mouse.loseFocus();
        } else {
            Client.setLowMemory(true);
            KeyBindingConfig.setDefaults();
        }

        return 1;
    }

    public long checkInterval() {
        return (long) Random.next(100, 600);
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isBackground() {
        return true;
    }
}
