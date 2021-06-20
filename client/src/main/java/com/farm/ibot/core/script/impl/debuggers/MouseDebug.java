package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BackgroundScript;
import com.farm.ibot.init.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class MouseDebug extends BackgroundScript implements PaintHandler {
    public static HashMap<Bot, HashMap<MouseDebug.ClickPoint, Integer>> clickPoints = new HashMap();
    private Image image;

    public static HashMap<MouseDebug.ClickPoint, Integer> getClicks() {
        if (!clickPoints.containsKey(Bot.get())) {
            clickPoints.put(Bot.get(), new HashMap());
        }

        return (HashMap) clickPoints.get(Bot.get());
    }

    public static void add(Point point) {
        getClicks().put(new MouseDebug.ClickPoint(point.x, point.y), (Integer) getClicks().getOrDefault(point, 0) + 1);
    }

    public void onStart() {
    }

    public int onLoop() {
        try {
            if (this.image == null) {
                this.image = ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream("cursor.png"));
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        return 5000;
    }

    public void onPaint(Graphics g) {
        int x = Mouse.getLocation().x;
        int y = Mouse.getLocation().y;
        g.drawImage(this.image, x, y, (ImageObserver) null);
    }

    public static class ClickPoint extends Point {
        public ClickPoint(int x, int y) {
            super(x, y);
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.x, this.y});
        }
    }
}
