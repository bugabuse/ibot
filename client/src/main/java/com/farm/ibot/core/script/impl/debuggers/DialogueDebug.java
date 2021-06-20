package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;

public class DialogueDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Npc message: " + Dialogue.getNpcMessage());
        this.drawString(g, "Can continue: " + Dialogue.canClickContinue());
        this.drawString(g, "In dialogue " + Dialogue.isInDialouge());
        this.drawString(g, "In cutscene " + Dialogue.isInTheCutScene());
        this.drawString(g, "Options:");
        String[] var2 = Dialogue.getOptions();
        int var3 = var2.length;

        int var4;
        for (var4 = 0; var4 < var3; ++var4) {
            String str = var2[var4];
            this.drawString(g, str);
        }

        Widget widget = Widgets.get(219, 0);
        if (widget != null) {
            Widget[] var9 = widget.getChildren();
            var4 = var9.length;

            for (int var10 = 0; var10 < var4; ++var10) {
                Widget child = var9[var10];
                if (child.getTextureId() == -1 && child.getScreenY() > 360 && child.getText() != null && child.getText().length() > 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(Color.red);
                }

                Rectangle bounds = child.getBounds();
                g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
                g.drawString(child.getTextureId() + " | " + child.getWidth() + " | " + child.getText(), bounds.x, bounds.y);
            }
        }

    }
}
