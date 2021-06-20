package com.farm.ibot.api.methods.input;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.GameShell;
import com.farm.ibot.api.accessors.MouseTracker;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.impl.debuggers.MouseDebug;
import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.api.SystemCalls;
import com.github.joonasvali.naturalmouse.support.DefaultMouseMotionNature;
import com.github.joonasvali.naturalmouse.support.ScreenAdjustedNature;
import com.github.joonasvali.naturalmouse.util.FactoryTemplates;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Mouse implements MouseListener, MouseMotionListener, FocusListener {
    public int x = -1;
    public int y = -1;
    public boolean mouseEntered = true;
    public boolean focused = true;

    public Mouse() {
        this.addListeners();
    }

    private static MouseMotionListener getMotionListener() {
        Bot.get().getCanvasHandler().enableInputMouse();
        return (MouseMotionListener) Client.getMouse();
    }

    private static MouseListener getMouseListener() {
        Bot.get().getCanvasHandler().enableInputMouse();
        return (MouseListener) Client.getMouse();
    }

    public static Mouse getMouse() {
        Bot.get().getCanvasHandler().enableInputMouse();
        return Bot.get().getMouse();
    }

    public static void move(Point point) {
        move(point.x, point.y);
    }

    public static void move(int x, int y) {
        if (Interact.useNaturalMouse) {
            naturalMove(x, y);
        } else {
            naturalMoveFaked(x, y);
        }

    }

    public static void naturalMoveFaked(int x, int y) {
        try {
            ArrayList<Pair<Point, Long>> mousePathToDestination = getMousePathToDestination(x, y);
            long wholeMovementTime = (Long) ((Pair) mousePathToDestination.get(mousePathToDestination.size() - 1)).getValue() - (Long) ((Pair) mousePathToDestination.get(0)).getValue();

            MouseTracker tracker = MouseTracker.getMouseTracker();
            int index = tracker.getCurrentIndex();
            Pair<Point, Long> lastMovement = null;
            Iterator var8 = mousePathToDestination.iterator();

            label24:
            while (true) {
                Pair pointLongPair;
                do {
                    if (!var8.hasNext()) {
                        break label24;
                    }

                    pointLongPair = (Pair) var8.next();
                } while (lastMovement != null && (Long) pointLongPair.getValue() - (Long) lastMovement.getValue() < 40L);

                tracker.getXCoordinates()[index] = ((Point) pointLongPair.getKey()).x;
                tracker.getYCoordinates()[index] = ((Point) pointLongPair.getKey()).y;
                tracker.getTimes()[index] = (Long) pointLongPair.getValue() - wholeMovementTime;
                ++index;
                lastMovement = pointLongPair;
            }
        } catch (Exception var10) {
        }

        hop(x, y);
    }

    public static ArrayList<Pair<Point, Long>> getMousePathToDestination(int x, int y) {
        try {
            final Point currentMousePoint = getLocation();
            MouseInfoAccessor mouseInfo = () -> {
                return currentMousePoint;
            };
            final long[] currentTime = new long[]{System.currentTimeMillis()};
            final ArrayList<Pair<Point, Long>> points = new ArrayList();
            SystemCalls systemCalls = new SystemCalls() {
                public long currentTimeMillis() {
                    return currentTime[0];
                }

                public void sleep(long time) throws InterruptedException {
                    long[] var10000 = currentTime;
                    var10000[0] += time;
                }

                public Dimension getScreenSize() {
                    return Toolkit.getDefaultToolkit().getScreenSize();
                }

                public void setMousePosition(int x, int y) {
                    currentMousePoint.setLocation(x, y);
                    Pair<Point, Long> objectObjectPair = new MutablePair(new Point(x, y), currentTime[0]);
                    points.add(objectObjectPair);
                }
            };
            MouseMotionFactory mouseMotionFactory = FactoryTemplates.createAverageComputerUserMotionFactory();
            mouseMotionFactory.setMouseInfo(mouseInfo);
            mouseMotionFactory.setSystemCalls(systemCalls);
            mouseMotionFactory.move(x, y);
            return points;
        } catch (Exception var8) {
            var8.printStackTrace();
            return new ArrayList();
        }
    }

    public static void hop(int x, int y, long time) {
        enterMouse();
        getMouse().x = x;
        getMouse().y = y;
        getMotionListener().mouseMoved(new MouseEvent(Client.getOriginalCanvas(), 503, time, 0, x, y, 0, false));
    }

    public static void hop(int x, int y) {
        enterMouse();
        getMouse().x = x;
        getMouse().y = y;
        getMotionListener().mouseMoved(new MouseEvent(Client.getOriginalCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false));
    }

    public static void click(int x, int y) {
        click(x, y, true);
    }

    public static void click(boolean leftClick) {
        gainFocus();
        int x = getLocation().x;
        int y = getLocation().y;
        if (leftClick) {
            MouseDebug.add(new Point(x, y));
        }

        int button = leftClick ? 1 : 3;
        press(x, y, button, 0);
        Time.waitNextGameCycle();
        release(x, y, button, 0);
        Time.waitNextGameCycle();
        getMouseListener().mouseClicked(new MouseEvent(Client.getOriginalCanvas(), 500, System.currentTimeMillis(), 0, x, y, 1, false, button));
        Time.waitNextGameCycle();
    }

    public static void click(int x, int y, boolean leftClick) {
        gainFocus();
        if (leftClick) {
            MouseDebug.add(new Point(x, y));
        }

        move(x, y);
        Time.waitNextGameCycle();
        int button = leftClick ? 1 : 3;
        press(x, y, button, 0);
        Time.waitNextGameCycle();
        release(x, y, button, 0);
        Time.waitNextGameCycle();
        getMouseListener().mouseClicked(new MouseEvent(Client.getOriginalCanvas(), 500, System.currentTimeMillis(), 0, x, y, 1, false, button));
        Time.waitNextGameCycle();
    }

    public static void click(int button) {
        MouseDebug.add(new Point(getLocation().x, getLocation().y));
        press(getLocation().x, getLocation().y, button, 0);
        Time.waitNextGameCycle();
        release(getLocation().x, getLocation().y, button, 0);
        Time.waitNextGameCycle();
        getMouseListener().mouseClicked(new MouseEvent(Client.getOriginalCanvas(), 500, System.currentTimeMillis(), 0, getLocation().x, getLocation().y, 1, false, button));
        Time.waitNextGameCycle();
    }

    public static void press(int x, int y, int button, int timeOffset) {
        getMouse().x = x;
        getMouse().y = y;
        getMouseListener().mousePressed(new MouseEvent(Client.getOriginalCanvas(), 501, System.currentTimeMillis() + (long) timeOffset, 0, x, y, 1, false, button));
    }

    public static void release(int x, int y, int button, int timeOffset) {
        getMouse().x = x;
        getMouse().y = y;
        getMouseListener().mouseReleased(new MouseEvent(Client.getOriginalCanvas(), 502, System.currentTimeMillis() + (long) timeOffset, 0, x, y, 1, false, button));
    }

    public static Point getLocation() {
        return new Point(getMouse().x, getMouse().y) {
            public String toString() {
                return "(" + this.x + ", " + this.y + ")";
            }
        };
    }

    public static void hopWithFocusHandler(int x, int y) {
        if (getLocation().x == -1 && getLocation().y == -1) {
            gainFocus();

        }

        hop(x, y);
    }

    public static void naturalMove(int x, int y) {
        try {
            new DefaultMouseMotionNature();
            MouseInfoAccessor mouseInfo = () -> {
                return getLocation();
            };
            SystemCalls systemCalls = new SystemCalls() {
                public long currentTimeMillis() {
                    return System.currentTimeMillis();
                }

                public void sleep(long time) throws InterruptedException {
                    Time.sleep(time);
                }

                public Dimension getScreenSize() {
                    return Toolkit.getDefaultToolkit().getScreenSize();
                }

                public void setMousePosition(int x, int y) {
                    Mouse.hop(x, y);
                }
            };
            MouseMotionFactory mouseMotionFactory = FactoryTemplates.createAverageComputerUserMotionFactory();
            mouseMotionFactory.setMouseInfo(mouseInfo);
            mouseMotionFactory.setSystemCalls(systemCalls);
            mouseMotionFactory.move(x, y);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void scroll(int rotation) {
        MouseWheelListener[] var1 = Client.getOriginalCanvas().getMouseWheelListeners();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            MouseWheelListener mouseWheelListener = var1[var3];

            mouseWheelListener.mouseWheelMoved(new MouseWheelEvent(Client.getOriginalCanvas(), 507, System.currentTimeMillis(), 0, getLocation().x, getLocation().y, 1, false, 1, 10, rotation));
        }

    }

    public static void clickBox(int x, int y, int x1, int y1) {
        clickBox(true, x, y, x1, y1);
    }

    public static void clickBox(boolean leftClick, int x, int y, int x1, int y1) {
        Rectangle rect = new Rectangle(x, y, x1 - x, y1 - y);
        if (!rect.contains(getLocation())) {
            Point p = Random.human(rect);
            click(p.x, p.y, leftClick);
        } else {
            click(leftClick);
        }

    }

    public static boolean isOffScreen(int x, int y) {
        return x < 15 || x > Screen.GAME_SCREEN.width - 15 || y < 15 || y > Screen.GAME_SCREEN.height - 15;
    }

    public static void loseFocus() {
        if (getMouse().focused) {

            FocusListener[] var0 = GameShell.getInstance().getCanvas().getFocusListeners();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                FocusListener focusListener = var0[var2];
                focusListener.focusLost(new FocusEvent(GameShell.getInstance().getCanvas(), 1005));
            }

        }
    }

    public static void gainFocus() {
        if (!getMouse().focused) {

            FocusListener[] var0 = GameShell.getInstance().getCanvas().getFocusListeners();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                FocusListener focusListener = var0[var2];
                focusListener.focusGained(new FocusEvent(GameShell.getInstance().getCanvas(), 1004));
            }

        }
    }

    public static void enterMouse() {
        if (!getMouse().mouseEntered) {

            MouseListener[] var0 = GameShell.getInstance().getCanvas().getMouseListeners();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                MouseListener listener = var0[var2];
                listener.mouseEntered(new MouseEvent(GameShell.getInstance().getCanvas(), 504, System.currentTimeMillis(), 0, 0, 0, 1, false, 0));
            }

        }
    }

    public static void exitMouse() {
        if (!getMouse().mouseEntered) {
        }


        MouseListener[] var0 = GameShell.getInstance().getCanvas().getMouseListeners();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            MouseListener listener = var0[var2];
            listener.mouseExited(new MouseEvent(GameShell.getInstance().getCanvas(), 505, System.currentTimeMillis(), 0, 0, 0, 1, false, 0));
        }

    }

    public static void moveOffScreen() {
        if (getMouse().mouseEntered) {
            Point point = randomOffscreenStartPoint();
            move(point.x, point.y);
            exitMouse();
        }
    }

    private static Point randomOffscreenStartPoint() {
        Point newPoint = new Point(1, 1);
        int corner = Random.next(0, 5);
        if (corner == 0) {
            newPoint.x = Random.next(0, Screen.CLIENT_SCREEN.width);
            newPoint.y = 0;
        } else if (corner == 1) {
            newPoint.x = 0;
            newPoint.y = Random.next(0, Screen.CLIENT_SCREEN.height);
        } else if (corner == 2) {
            newPoint.x = Random.next(0, Screen.CLIENT_SCREEN.width);
            newPoint.y = Screen.CLIENT_SCREEN.height - 1;
        } else {
            newPoint.x = Screen.CLIENT_SCREEN.width - 1;
            newPoint.y = Random.next(0, Screen.CLIENT_SCREEN.height);
        }

        return newPoint;
    }

    public void addListeners() {
        Client.getOriginalCanvas().addMouseListener(this);
        Client.getOriginalCanvas().addMouseMotionListener(this);
        Client.getOriginalCanvas().addFocusListener(this);
    }

    public void mouseMoved(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

        getMouse().mouseEntered = true;
    }

    public void mouseExited(MouseEvent e) {

        getMouse().mouseEntered = false;
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void focusGained(FocusEvent e) {

        if (getMouse() != null) {
            getMouse().focused = true;
        }

    }

    public void focusLost(FocusEvent e) {

        if (getMouse() != null) {
            getMouse().focused = false;
        }

    }
}
