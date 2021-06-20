package com.farm.ibot.api.util;

import com.farm.ibot.api.wrapper.Tile;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class PaintUtils {
    public static void textOnTile(Graphics g, Tile tile, String str) {
        if (tile != null) {
            textOnPolygon(g, tile.getBounds(), str);
        }

    }

    public static void textOnPolygon(Graphics g, Polygon bounds, String str) {
        int width = g.getFontMetrics().stringWidth(str) / 2;
        int height = g.getFontMetrics().getHeight() / 2;
        int x = (int) bounds.getBounds2D().getCenterX() - width;
        int y = (int) bounds.getBounds2D().getCenterY() - height;
        g.drawString(str, x, y);
    }

    public static void drawTile(Graphics g, Tile tile, String str) {
        drawTile(g, tile);
        textOnTile(g, tile, str);
    }

    public static void drawTile(Graphics g, Tile tile) {
        if (tile != null) {
            g.drawPolygon(tile.getBounds());
        }

    }

    public static void fillTile(Graphics g, Tile tile) {
        if (tile != null) {
            g.fillPolygon(tile.getBounds());
        }

    }

    public static String convertMillisToString(long millis) {
        String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return time;
    }
}
