// Decompiled with: Procyon 0.5.36
package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.accessors.World;
import com.farm.ibot.api.interact.action.impl.WidgetAction;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.Bot;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

public class WorldHopping {
    public static int[] F2P_WORLDS;
    public static int[] P2P_WORLDS;
    private static Point firstWorldCached;
    private static Point rectSizeCached;
    private static long lastFirstWorldCached;
    private static long lastRrectSizeCached;

    static {
        WorldHopping.firstWorldCached = null;
        WorldHopping.rectSizeCached = null;
        WorldHopping.lastFirstWorldCached = 0L;
        WorldHopping.lastRrectSizeCached = 0L;
        WorldHopping.F2P_WORLDS = new int[]{1, 8, 16, 26, 35, 79, 80, 82, 83, 84, 93, 94, 97, 98, 99, 118, 125, 126, 130, 131, 133, 134, 135, 136, 137, 138, 139, 140, 151, 152, 153, 154, 155, 156, 157, 158, 159, 169, 170, 171, 172, 173, 174, 175, 176, 197, 198, 199, 200, 201, 202, 203, 204};
        WorldHopping.P2P_WORLDS = new int[]{2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 17, 18, 20, 21, 22, 23, 27, 28, 29, 30, 31, 32, 33, 36, 38, 39, 40, 41, 42, 44, 46, 47, 48, 50, 51, 52, 55, 56, 57, 58, 59, 60, 62, 67, 68, 69, 70, 74, 75, 76, 77, 78, 86, 87, 88, 89, 90, 95, 121, 122, 124, 144, 145, 165, 166, 191, 193, 194, 195, 196, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225};
    }

    public static int toRegularWorldNumber(int world) {
        if (world >= 300) {
            world -= 300;
        }
        return world;
    }

    public static int toExpandedWorldNumber(int world) {
        if (world < 300) {
            world += 300;
        }
        return world;
    }

    public static boolean hop(int world) {
        if (world < 300) {
            world += 300;
        }

        if (Client.getCurrentWorld() == world) {
            return true;
        }
        if (Client.isInGame()) {
            return quickHop(world);
        }
        if (Login.getState() != Login.ScreenState.WORLDHOP && !openWorldHop()) {
            return false;
        }
        final int finalWorld = world;
        Client.switchToWorld(Client.getWorlds(w -> w.getId() == finalWorld).get(0));
        Time.waitNextGameCycle();
        Time.sleep(2500, () -> Login.getState() != Login.ScreenState.WORLDHOP);
        return Time.sleep(1500, () -> Client.getCurrentWorld() == finalWorld);
    }

    private static boolean quickHop(int world) {

        world = toExpandedWorldNumber(world);
        if (Client.getCurrentWorld() == world) {

            return true;
        }

        if (Client.isInGame()) {

            if (Widgets.closeTopInterface()) {

                if (Widgets.get(69, 16, 0) == null) {

                    if (GameTab.LOGOUT.open()) {

                        Time.sleep(() -> Widgets.get(182, 3) != null);
                        final Widget widget = Widgets.get(182, 3);
                        if (widget != null) {

                            widget.interact("World Switcher");
                            Time.sleep(() -> Widgets.get(69, 16, 0) != null);
                        }
                    }
                }

                if (Widgets.get(69, 16, 0) != null) {

                    WidgetAction.create(4522000, world).sendByMouse();
                    final int w = world;
                    return Time.sleep(() -> Client.getCurrentWorld() == w);
                }
            }
        }
        return false;
    }

    public static boolean openWorldHop() {
        Mouse.clickBox(12, 467, 104, 497);
        Time.waitNextGameCycle();
        if (!Time.sleep(10000, () -> Login.getState() == Login.ScreenState.WORLDHOP)) {
            Time.waitNextGameCycle();
            return false;
        }
        Time.waitNextGameCycle();
        return true;
    }

    private static Point getFirstWorld() {
        if (WorldHopping.firstWorldCached == null || System.currentTimeMillis() - WorldHopping.lastFirstWorldCached > 10L) {
            WorldHopping.firstWorldCached = cacheFirstWorld();
            WorldHopping.lastFirstWorldCached = System.currentTimeMillis();
        }
        return WorldHopping.firstWorldCached;
    }

    private static Point getRectSize() {
        if (WorldHopping.firstWorldCached == null || System.currentTimeMillis() - WorldHopping.lastRrectSizeCached > 10L) {
            WorldHopping.rectSizeCached = cacheRectSize();
            WorldHopping.lastRrectSizeCached = System.currentTimeMillis();
        }
        return WorldHopping.rectSizeCached;
    }

    private static Point cacheFirstWorld() {
        final Bot bot = Bot.get();
        for (int y = 24; y < 502; ++y) {
            for (int x = 0; x < 750; ++x) {
                if (!Screen.getColorAt(x, y, bot).equals(Color.black)) {
                    return new Point(x - 5, y);
                }
            }
        }
        return new Point(-1, -1);
    }

    private static Point cacheRectSize() {
        Point lastWorld = new Point();
        final Bot bot = Bot.get();
        Label_0072:
        for (int x = 750; x > 0; x -= 3) {
            for (int y = 502; y > 24; y -= 3) {
                if (!Screen.getColorAt(x, y, bot).equals(Color.black)) {
                    lastWorld = new Point(x - 5, y);
                    break Label_0072;
                }
            }
        }
        for (int x = 0; x < 750; x += 3) {
            for (int y = 502; y > 24; y -= 3) {
                if (!Screen.getColorAt(x, y, bot).equals(Color.black)) {
                    lastWorld.y = y + 6;
                    return lastWorld;
                }
            }
        }
        return lastWorld;
    }

    public static Rectangle getWorldSelectRect() {
        final Point p1 = getFirstWorld();
        final Point p2 = getRectSize();
        final Point point = new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
        final Point size = new Point(Math.max(p1.x, p2.x) - point.x, Math.max(p1.y, p2.y) - point.y);
        return new Rectangle(point.x, point.y, size.x + 5, size.y);
    }

    private static int getCollumnCount() {
        return getWorldSelectRect().width / 90;
    }

    private static int getRowCount() {
        return (int) (getWorldSelectRect().getHeight() / 22.8);
    }

    public static Rectangle getRectangleByIndex(final int index) {
        final int xOffset = getFirstWorld().x;
        int yOffset = getFirstWorld().y;
        int yMultipler = 24;
        if (getRowCount() == 20) {
            yOffset = 34;
            yMultipler = 23;
        }
        if (getRowCount() == 23) {
            yOffset = 34;
            yMultipler = 18;
        }
        final int x = (int) (Math.floor(index) / getRowCount() % getCollumnCount()) * 93 + xOffset;
        final int y = (int) (Math.ceil(index) % getRowCount()) * yMultipler + yOffset;
        return new Rectangle(x, y, 90, 18);
    }

    public static Rectangle getRectangle(int world) {
        world = toRegularWorldNumber(world);
        final World[] worlds = Client.getWorlds();
        for (int i = 0; i < worlds.length; ++i) {
            if (worlds[i].getRegularId() == world) {
                return getRectangleByIndex(i);
            }
        }
        return new Rectangle(0, 0, 0, 0);
    }

    public static boolean isHovered(final Rectangle rect) {
        return getRectangleBrightness(rect) > 390;
    }

    public static int getRectangleBrightness(final Rectangle rect) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int x = 0; x < rect.width; ++x) {
            for (int y = 0; y < rect.height; ++y) {
                final Color c = Screen.getColorAt(rect.x + x, rect.y + y);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        final int count = rect.height * rect.width;
        final int col = (r + g + b) / count;
        return col;
    }

    public static int getRandomF2p() {
        final List<Integer> worlds = Client.getF2pWorlds();
        return worlds.get(new Random().nextInt(worlds.size()));
    }

    public static int getRandomP2p() {
        final List<Integer> worlds = Client.getP2pWorlds();
        return worlds.get(new Random().nextInt(worlds.size()));
    }

    public static boolean isF2p(final int world) {
        if (Client.getF2pWorlds().size() > 0) {
            WorldHopping.F2P_WORLDS = Client.getF2pWorlds().stream().mapToInt((ToIntFunction<Integer>) WorldHopping::toRegularWorldNumber).toArray();
        }
        if (Client.getP2pWorlds().size() > 0) {
            WorldHopping.P2P_WORLDS = Client.getP2pWorlds().stream().mapToInt((ToIntFunction<Integer>) WorldHopping::toRegularWorldNumber).toArray();
        }
        final int regWorld = toRegularWorldNumber(world);
        if (Bot.get().isLoaded() && !Client.isInGame()) {
            return Client.getWorlds(w -> w.isSafeF2p() && toRegularWorldNumber(w.getId()) == regWorld).size() > 0;
        }
        return Arrays.stream(WorldHopping.F2P_WORLDS).anyMatch(i -> i == regWorld) || Arrays.stream(WorldHopping.F2P_WORLDS).anyMatch(i -> 300 + i == regWorld);
    }

    public static boolean equals(final int world1, final int world2) {
        return toRegularWorldNumber(world1) == toRegularWorldNumber(world2);
    }
}
