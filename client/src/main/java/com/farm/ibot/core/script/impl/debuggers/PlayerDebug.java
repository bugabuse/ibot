package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.AttackMode;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.Iterator;

public class PlayerDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Attack mode: " + AttackMode.get());
        this.drawString(g, "Position: " + Player.getLocal().getPosition() + " | " + Player.getLocal().getAnimablePosition() + " | " + Player.getLocal().getAnimablePosition().toLocalTile());
        this.drawString(g, "Animation: " + Player.getLocal().getAnimation());
        int[] appearance = Player.getLocal().getComposite().getAppearance();
        String str = "";
        int[] var4 = appearance;
        int var5 = appearance.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            int i = var4[var6];
            str = str + (i - 512) + ", ";
        }

        this.drawString(g, "Appearance: [" + str + "]");
        Iterator var8 = Players.getAll().iterator();

        while (var8.hasNext()) {
            Player player = (Player) var8.next();
            g.drawString(player.getName() + " (" + player.getIndex() + ")", player.getScreenPoint().x, player.getScreenPoint().y);
        }

    }
}
