package com.farm.ibot.api.interact.action;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Menu;
import com.farm.ibot.api.interact.action.impl.ItemAction;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.SeedRandom;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.reflection.Reflection;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Action {
    public static boolean isSpamClicking;
    private static ExecutorService clickExecutor = Executors.newFixedThreadPool(1, (r) -> {
        return new Thread("Click");
    });
    public int arg1;
    public int arg2;
    public int arg3;
    public int arg4;
    public String sArg1;
    public String sArg2;
    public int clickPointX;
    public int clickPointY;
    public String description;

    public Action() {
        this(-1, -1, -1, -1, "", "", -1, -1);
    }

    public Action(String description) {
        this(-1, -1, -1, -1, "", "", -1, -1);
        this.description = description;
    }

    public Action(int arg1, int arg2, int arg3, int arg4, String sArg1, String sArg2, int clickPointX, int clickPointY) {
        this.description = "";
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.sArg1 = sArg1;
        this.sArg2 = sArg2;
        this.clickPointX = clickPointX;
        this.clickPointY = clickPointY;
    }

    private ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList();
        ArrayList<Point> rootClicks = new ArrayList();
        int seed = AccountData.current() != null ? AccountData.current().id : Bot.get().hashCode();
        SeedRandom rnd = new SeedRandom((long) seed);

        int i;
        for (i = 0; i < 40; ++i) {
            rootClicks.add(rnd.next(Screen.CLIENT_SCREEN));
        }

        Point toRandomize;
        for (i = 0; i < 200; ++i) {
            toRandomize = (Point) rootClicks.get(rnd.next(0, rootClicks.size()));
            rootClicks.add(new Point(toRandomize.x + rnd.next(-15, 15), toRandomize.y + rnd.next(-15, 15)));
        }

        for (i = 0; i < 2000; ++i) {
            toRandomize = (Point) rootClicks.get(rnd.next(0, rootClicks.size()));
            points.add(new Point(toRandomize.x + rnd.next(-6, 6), toRandomize.y + Random.next(-6, 6)));
        }

        return points;
    }

    public void send() {
        if (this.arg1 == -1 && this.arg2 == -1 && this.arg3 == -1 && this.arg4 == -1 && this.clickPointX == -1 && this.clickPointY == -1) {


            try {
                throw new EmptyActionException("Cannot send empty action, " + this.description);
            } catch (EmptyActionException var2) {
                var2.printStackTrace();
            }
        } else {
            if (this.clickPointX < 0) {
                this.clickPointX = 0;
            }

            if (this.clickPointY < 0) {
                this.clickPointY = 0;
            }

            this.clickPointX = Random.next(1, Screen.GAME_SCREEN.width);
            this.clickPointY = Random.next(1, Screen.GAME_SCREEN.height);
            Debug.log("Process menu(" + this.getClass().getSimpleName() + ") action: " + this.arg1 + "," + this.arg2 + "," + this.arg3 + "," + this.arg4 + "," + this.sArg1 + "," + this.sArg2 + "," + this.clickPointX + "," + this.clickPointY);
            this.doAction();
        }
    }

    public void doAction() {
        Reflection.setValue((String) "client.allowInput", false, null);
        this.fakeClick();
        Client.processMenuAction(this.arg1, this.arg2, this.arg3, this.arg4, this.sArg1, this.sArg2, this.clickPointX, this.clickPointY);
    }

    private void fakeClick() {
        if (!Settings.useHumanData) {
            Mouse.naturalMoveFaked(this.clickPointX, this.clickPointY);
            Mouse.click(this.clickPointX, this.clickPointY, true);
        }

    }

    public void sendByMouse() {
        if (this.arg1 == -1 && this.arg2 == -1 && this.arg3 == -1 && this.arg4 == -1 && this.clickPointX == -1 && this.clickPointY == -1) {


            try {
                throw new EmptyActionException("Cannot send empty action, " + this.description);
            } catch (EmptyActionException var4) {
                var4.printStackTrace();
            }
        } else {
            this.sArg1 = "Perform";
            this.sArg2 = "<col=ff00ff>Action";
            Rectangle clientScreen = new Rectangle(Screen.GAME_SCREEN);
            clientScreen.translate(20, 20);
            if (!(this instanceof ItemAction)) {
                ArrayList<Point> points = this.getPoints();
                Point p = (Point) this.getPoints().get(Random.next(0, points.size() - 1));
                Mouse.move(p.x, p.y);
                Time.sleep(0, 300);
            }

            if (Menu.open()) {
                if (Arrays.stream(Menu.getNodes()).anyMatch((menuNode) -> {
                    return menuNode.getAction().contains("Action");
                })) {
                    Menu.close();
                    return;
                }

                Menu.addItem(this.arg1, this.arg2, this.arg3, this.arg4, this.sArg1, this.sArg2);
                Time.waitNextGameCycle();
                Time.waitNextGameCycle();
                Time.waitNextGameCycle();
                Point point = new Point(-1, -1);
                Time.sleep(2000, () -> {
                    Point p = Screen.findPixel(new Color(255, 0, 255), Menu.getBounds());
                    if (p != null) {
                        point.setLocation(p.x, p.y);
                        return true;
                    } else {
                        return false;
                    }
                });
                if (point.getX() != -1.0D && point.getY() != -1.0D) {
                    Mouse.move(point.x, point.y);
                    Time.waitNextGameCycle();
                    Mouse.click(true);
                }
            }

        }
    }
}
