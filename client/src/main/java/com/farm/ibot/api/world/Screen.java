package com.farm.ibot.api.world;

import com.farm.ibot.api.accessors.Camera;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.interfaces.Animable;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen {
    public static final Rectangle GAME_SCREEN = new Rectangle(0, 0, 517, 336);
    public static final Rectangle CLIENT_SCREEN = new Rectangle(0, 0, 768, 512);
    public static final int TILE_FLAG_BRIDGE = 2;
    public static final Polygon MINIMAP_POLYGON;
    public static final int MAX_Z = 4;
    public static final int LOCAL_COORD_BITS = 7;
    public static final int LOCAL_TILE_SIZE = 128;
    public static final int LOCAL_HALF_TILE_SIZE = 64;
    public static int[] SINE = new int[2048];
    public static int[] COSINE = new int[2048];

    static {
        for (int i = 0; i < SINE.length; ++i) {
            SINE[i] = (int) (65536.0D * Math.sin((double) i * 0.0030679615D));
            COSINE[i] = (int) (65536.0D * Math.cos((double) i * 0.0030679615D));
        }

        MINIMAP_POLYGON = new Polygon(new int[]{583, 610, 625, 632, 652, 661, 680, 701, 714, 716, 709, 695, 679, 659, 637, 614, 590, 574, 571, 577}, new int[]{114, 129, 143, 157, 157, 143, 128, 118, 96, 65, 45, 28, 17, 10, 8, 14, 31, 54, 83, 103}, 20);
    }

    public static boolean isOnGameScreen(Point p) {
        return p != null && GAME_SCREEN.contains(p);
    }

    public static boolean isOnMinimap(Tile tile) {
        return MINIMAP_POLYGON.contains(tile.getMinimapPoint());
    }

    public static Point tileToScreen(Tile tile) {
        return worldToScreen(tile.getX(), tile.getY(), 0);
    }

    public static Point worldToScreen(Animable animable, Renderable renderable) {
        return worldToScreen(animable.getAnimableX(), animable.getAnimableY(), renderable.getModelHeight());
    }

    public static Point worldToScreen(int regionX, int regionY, int height) {
        if (regionX >= 128 && regionY >= 128 && regionX <= 13056 && regionY <= 13056) {
            int z = getTileHeight(Client.getPlane(), regionX, regionY) - height;
            regionX -= Camera.getX();
            z -= Camera.getZ();
            regionY -= Camera.getY();
            int yaw = Camera.getYaw();
            int pitch = Camera.getPitch();
            int pitch_sin = SINE[pitch];
            int pitch_cos = COSINE[pitch];
            int yaw_sin = SINE[yaw];
            int yaw_cos = COSINE[yaw];
            int _angle = regionY * yaw_sin + regionX * yaw_cos >> 16;
            regionY = regionY * yaw_cos - regionX * yaw_sin >> 16;
            regionX = _angle;
            _angle = z * pitch_cos - regionY * pitch_sin >> 16;
            regionY = z * pitch_sin + regionY * pitch_cos >> 16;
            if (regionY >= 50) {
                Point point = new Point(258 + (regionX << 9) / regionY, (_angle << 9) / regionY + 170);
                return CLIENT_SCREEN.contains(point) ? point : new Point(0, 0);
            } else {
                return new Point(0, 0);
            }
        } else {
            return new Point(0, 0);
        }
    }

    public static Point worldToScreenNew(int x, int y, int z) {
        if (x >= 128 && y >= 128 && x <= 13056 && y <= 13056) {
            x -= Camera.getX();
            y -= Camera.getY();
            z -= Camera.getZ();
            int cameraPitch = Camera.getPitch();
            int cameraYaw = Camera.getYaw();
            int pitchSin = SINE[cameraPitch];
            int pitchCos = COSINE[cameraPitch];
            int yawSin = SINE[cameraYaw];
            int yawCos = COSINE[cameraYaw];
            int var8 = yawCos * x + y * yawSin >> 16;
            y = yawCos * y - yawSin * x >> 16;
            x = var8;
            var8 = pitchCos * z - y * pitchSin >> 16;
            y = z * pitchSin + y * pitchCos >> 16;
            if (y >= 50) {
                int pointX = Client.getViewportWidth() / 2 + x * Client.getZoom() / y;
                int pointY = Client.getViewportHeight() / 2 + var8 * Client.getZoom() / y;
                return new Point(pointX + Client.getViewportOffsetX(), pointY + Client.getViewportOffsetY());
            }
        }

        return null;
    }

    public static int getTileHeight(int plane, int sceneX, int sceneY) {
        if (sceneX >= 0 && sceneY >= 0 && sceneX < 104 && sceneY < 104) {
            byte[][][] tileSettings = Client.getTileFlags();
            int[][][] tileHeights = Client.getTileHeights();
            int z1 = plane;
            if (plane < 3 && (tileSettings[1][sceneX][sceneY] & 2) == 2) {
                z1 = plane + 1;
            }

            int x = sceneX & 127;
            int y = sceneY & 127;
            int var8 = x * tileHeights[z1][sceneX + 1][sceneY] + (128 - x) * tileHeights[z1][sceneX][sceneY] >> 7;
            int var9 = tileHeights[z1][sceneX][sceneY + 1] * (128 - x) + x * tileHeights[z1][sceneX + 1][sceneY + 1] >> 7;
            return (128 - y) * var8 + y * var9 >> 7;
        } else {
            return 0;
        }
    }

    public static int getTileHeightold(int plane, int x, int y) {
        int xx = x >> 7;
        int yy = y >> 7;
        if (xx >= 0 && yy >= 0 && xx <= 103 && yy <= 103) {
            int[][][] tileHeights = Client.getTileHeights();
            int aa = tileHeights[plane][xx][yy] * (128 - (x & 127)) + tileHeights[plane][xx + 1][yy] * (x & 127) >> 7;
            int ab = tileHeights[plane][xx][yy + 1] * (128 - (x & 127)) + tileHeights[plane][xx + 1][yy + 1] * (x & 127) >> 7;
            return aa * (128 - (y & 127)) + ab * (y & 127) >> 7;
        } else {
            return 0;
        }
    }

    public static Point worldToMap(int regionX, int regionY) {
        int mapScale = 0;
        int mapOffset = 0;
        int angle = Client.getMapAngle() + mapScale & 2047;
        int sin = SINE[angle] * 256 / (mapOffset + 256);
        int cos = COSINE[angle] * 256 / (mapOffset + 256);
        int xMap = regionY * sin + regionX * cos >> 16;
        int yMap = regionY * cos - regionX * sin >> 16;
        return new Point(644 + xMap, 80 - yMap);
    }

    public static Point tileToMap(Tile tile) {
        int xMapTile = tile.getX() - Client.getBaseX();
        int yMapTile = tile.getY() - Client.getBaseY();
        return worldToMap(xMapTile * 4 + 2 - Player.getLocal().getAnimableX() / 32, yMapTile * 4 + 2 - Player.getLocal().getAnimableY() / 32);
    }

    public static Point findPixel(Color color, Rectangle rectangle) {
        BufferedImage image = Bot.get().getGameImage();

        for (int x = rectangle.x; x < rectangle.x + rectangle.width && x < image.getWidth(); ++x) {
            for (int y = rectangle.y; y < rectangle.y + rectangle.height && y < image.getHeight(); ++y) {
                if (color.getRGB() == image.getRGB(x, y)) {
                    return new Point(x, y);
                }
            }
        }

        return null;
    }

    public static Color getColorAt(int x, int y) {
        BufferedImage image = Bot.get().getGameImage();
        return x > -1 && y > -1 && x < image.getWidth() && y < image.getHeight() ? new Color(image.getRGB(x, y)) : Color.black;
    }

    public static Color getColorAt(int x, int y, Bot bot) {
        BufferedImage image = bot.getGameImage();
        return x > -1 && y > -1 && x < image.getWidth() && y < image.getHeight() ? new Color(image.getRGB(x, y)) : Color.black;
    }

    public static Point centroid(Polygon[] polygons) {
        int x = 0;
        int y = 0;
        Polygon[] var3 = polygons;
        int var4 = polygons.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Polygon polygon = var3[var5];
            Point center = centroid(polygon);
            x += center.x;
            y += center.y;
        }

        if (polygons.length > 0) {
            x /= polygons.length;
            y /= polygons.length;
            return new Point(x, y);
        } else {
            return new Point(-1, -1);
        }
    }

    public static Point centroid(Polygon polygon) {
        int centroidX = 0;
        int centroidY = 0;

        for (int i = 0; i < polygon.npoints; ++i) {
            centroidX += polygon.xpoints[i];
            centroidY += polygon.ypoints[i];
        }

        return new Point(centroidX / polygon.npoints, centroidY / polygon.npoints);
    }

    public static boolean colorMatches(Color color, int tolerance, int x, int y) {
        Color screenColor = getColorAt(x, y);
        int rDiff = Math.abs(screenColor.getRed() - color.getRed());
        int bDiff = Math.abs(screenColor.getBlue() - color.getBlue());
        int dDiff = Math.abs(screenColor.getGreen() - color.getGreen());
        return rDiff + bDiff + dDiff <= tolerance;
    }
}
