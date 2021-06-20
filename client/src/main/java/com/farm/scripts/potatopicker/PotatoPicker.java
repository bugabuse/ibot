package com.farm.scripts.potatopicker;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.SeedRandom;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.Session;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class PotatoPicker extends BotScript implements PaintHandler, InventoryListener, ScriptRuntimeInfo, MouseListener {
    public PaintTimer timer = new PaintTimer();
    public int itemsGained = 0;
    ArrayList<Pair<Point, Long>> mousePathToDestination;
    private ArrayList<Point> points = new ArrayList();
    private ArrayList<GameObject> gameObjects;

    public void onStart() {

        Client.getOriginalCanvas().addMouseListener(this);

        try {
            AccountData[] accountsCache = Session.getAccountsCache();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\Administrator\\Desktop\\OSRSBOT\\accounts.json")));
            (new Gson()).toJson(accountsCache, writer);
            writer.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public int onLoop() {
        return 1000;
    }

    public void onPaint(Graphics g) {
    }

    private void shuffle2() {
        this.points.clear();
        ArrayList<Point> rootClicks = new ArrayList();
        SeedRandom rnd = new SeedRandom(2L);

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
            this.points.add(new Point(toRandomize.x + rnd.next(-6, 6), toRandomize.y + Random.next(-6, 6)));
        }

    }

    private ArrayList<Point> shufflePolygon(Polygon polygon) {
        long hash = 1L;
        Rectangle rect = polygon.getBounds();
        SeedRandom rnd = new SeedRandom(hash);
        ArrayList<Point> rootClicks = new ArrayList();
        int i = 0;

        Point toRandomize;
        while (i < 100) {
            toRandomize = new Point(rect.x + rnd.nextGaussian(0, rect.width), rect.y + rnd.nextGaussian(0, rect.height));
            if (polygon.contains(toRandomize)) {
                rootClicks.add(toRandomize);
                ++i;
            }
        }

        for (i = 0; i < 300; ++i) {
            toRandomize = (Point) rootClicks.get(rnd.next(0, rootClicks.size()));
            rootClicks.add(new Point(toRandomize.x + rnd.next(-40, 40), toRandomize.y + rnd.next(-40, 40)));
        }

        for (i = 0; i < 2000; ++i) {
            toRandomize = (Point) rootClicks.get(rnd.next(0, rootClicks.size()));
            rootClicks.add(new Point(toRandomize.x + Random.next(-6, 6), toRandomize.y + rnd.next(-6, 6)));
        }

        return rootClicks;
    }

    private ArrayList<Point> shuffleRectangle(Rectangle rect) {
        int hash = Arrays.hashCode(new int[]{rect.x, rect.y, rect.width, rect.height});
        ArrayList<Point> points = new ArrayList();
        SeedRandom rnd = new SeedRandom((long) hash);
        ArrayList<Point> rootClicks = new ArrayList();

        int i;
        for (i = 0; i < 10; ++i) {
            rootClicks.add(new Point(rect.x + rnd.nextGaussian(0, rect.width), rect.y + rnd.nextGaussian(0, rect.height)));
        }

        Point toRandomize;
        for (i = 0; i < 200; ++i) {
            toRandomize = (Point) rootClicks.get(rnd.next(0, rootClicks.size()));
            rootClicks.add(new Point(toRandomize.x + Random.next(-6, 6), toRandomize.y + rnd.next(-6, 6)));
        }

        for (i = 0; i < 1000; ++i) {
            toRandomize = (Point) rootClicks.get(rnd.next(0, rootClicks.size()));
            points.add(new Point(toRandomize.x + rnd.next(-3, 3), toRandomize.y + rnd.next(-3, 3)));
        }

        return points;
    }

    public void onItemAdded(Item item) {
    }

    public String runtimeInfo() {
        return "Runtime: " + this.timer.getElapsedString() + " Potatoes: " + this.itemsGained + "(" + this.timer.getHourRatio(this.itemsGained) + ")";
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
