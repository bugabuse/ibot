package com.farm.scripts.pathfinder;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BotScript;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class PathFinderTest extends BotScript implements PaintHandler, MouseListener {
    Graphics g;
    int gridSize = 15;
    int size = 25;
    ArrayList<Tile> obstacles = new ArrayList();
    ArrayList<Tile> visitedNodes = new ArrayList();
    Tile end = new Tile(10, 10, 0);
    ArrayList<Tile> path = new ArrayList();
    Tile last = new Tile(-1, -1, 0);
    ArrayList<Tile> excluded = new ArrayList();
    boolean stopAnimation = true;
    Rectangle obstaclesLoad = new Rectangle(400, 350, 60, 30);
    int[][] flags;

    private boolean updateFlags() {
        int[][] tempFlags = WorldData.getCollisionFlags(Client.getPlane());
        boolean flagsUpdated = false;

        for (int x = 0; x < tempFlags.length; ++x) {
            for (int y = 0; y < tempFlags[x].length; ++y) {
                if (this.flags == null) {
                    this.flags = new int[tempFlags.length][tempFlags[x].length];
                }

                if (this.flags.length > x && this.flags[x].length > y && this.flags[x][y] != tempFlags[x][y]) {
                    flagsUpdated = true;
                    this.flags[x][y] = tempFlags[x][y];
                }
            }
        }

        return flagsUpdated;
    }

    public void onStart() {
        this.addMouseListeners();
    }

    public int onLoop() {
        if (this.updateFlags()) {

        }

        return 1000;
    }

    public void onPaint(Graphics g) {
        this.g = g;
        g.fillRect(this.obstaclesLoad.x, this.obstaclesLoad.y, this.obstaclesLoad.width, this.obstaclesLoad.height);
        Iterator var2 = (new ArrayList(this.visitedNodes)).iterator();

        Tile t;
        while (var2.hasNext()) {
            t = (Tile) var2.next();
            this.drawTile(t, Color.YELLOW);
        }

        if (this.path != null) {
            var2 = (new ArrayList(this.path)).iterator();

            while (var2.hasNext()) {
                t = (Tile) var2.next();
                this.drawTile(t, Color.gray);
            }
        }

        var2 = (new ArrayList(this.obstacles)).iterator();

        while (var2.hasNext()) {
            t = (Tile) var2.next();
            this.drawTile(t, Color.red);
        }

        for (int x = 0; x < this.gridSize; ++x) {
            for (int y = 0; y < this.gridSize; ++y) {
                t = new Tile(x, y);
                this.drawTile(t, new Color(0, 255, 0, 100));
                if (this.excluded.contains(t)) {
                    this.drawTileText(t, "/" + (int) this.getDist(t, this.end));
                } else {
                    this.drawTileText(t, "" + (int) this.getDist(t, this.end));
                }
            }
        }

        if (this.last != null) {
            this.drawTile(this.last, Color.pink);
            this.drawTileText(this.last, "" + (int) this.getDist(this.last, this.end));
        }

        this.drawTile(this.end, Color.blue);
        this.drawTile(new Tile(1, 1, 0), Color.blue);
    }

    private double getDist(Tile t1, Tile t2) {
        return this.obstacles.contains(t1) ? -1.0D : (double) t1.distance(t2);
    }

    private void drawTile(Tile t, Color color) {
        t = new Tile(t.getX() % this.gridSize, t.getY() % this.gridSize);
        this.g.setColor(color);
        this.g.fillRect(t.getX() * this.size, (this.gridSize - t.getY()) * this.size, this.size, this.size);
        this.g.drawRect(t.getX() * this.size, (this.gridSize - t.getY()) * this.size, this.size, this.size);
    }

    private void drawTileText(Tile t, String text) {
        t = new Tile(t.getX() % this.gridSize, t.getY() % this.gridSize);
        this.g.setColor(Color.red);
        this.g.drawString(text, t.getX() * this.size + 3, (this.gridSize - t.getY()) * this.size + this.size - 3);
    }

    ArrayList<Tile> findPath(Tile start, Tile end) {
        this.visitedNodes.clear();
        this.path = null;
        start = start.toLocalTile();
        end = end.toLocalTile();
        ArrayList<Tile> visited = new ArrayList();
        this.excluded = new ArrayList();
        Tile current = start;
        ArrayList<Tile> finalPath = new ArrayList();
        visited.add(start);

        while (true) {
            label60:
            while (true) {
                label46:
                do {
                    while (current != null) {
                        Time.sleep(100);
                        Debug.log(visited.size() + " " + this.excluded.size());
                        Tile last = current;
                        current = this.getBestNeighbor(current, end, this.excluded);

                        if (current == null) {

                            this.excluded.add(last);
                            if (visited.size() > 0) {
                                current = (Tile) visited.get(visited.size() - 1);
                            }
                            continue label46;
                        }

                        Tile temp = this.getBestNeighbor(current, end, this.excluded);
                        if (temp != null && temp.equals(last)) {
                            this.excluded.add(temp);
                            finalPath.add(current);
                            if (current.distance(end) == 0) {

                                return finalPath;
                            }
                        } else {
                            finalPath.add(current);
                            visited.add(current);
                            this.visitedNodes.add(current);
                            this.last = current;
                            if (current.distance(end) == 0) {
                                return finalPath;
                            }
                        }
                    }

                    return finalPath;
                } while (this.getBestNeighbor(current, end, this.excluded) != null);

                int i;
                for (i = visited.size() - 1; i >= 0; --i) {
                    if (!this.excluded.contains(visited.get(i))) {
                        current = (Tile) visited.get(i);
                        continue label60;
                    }
                }

                for (i = visited.size() - 1; i >= 0; --i) {
                    if (this.getBestNeighbor((Tile) visited.get(i), end, this.excluded) != null) {
                        current = (Tile) visited.get(i);
                        continue label60;
                    }
                }


                return null;
            }
        }
    }

    private Tile getBestNeighbor(Tile current, Tile end, ArrayList<Tile> exluded) {
        if (current == null) {
            return null;
        } else {
            Tile best = null;
            double bestDistance = 2.147483647E9D;

            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    if (x != 0 || y != 0) {
                        Tile t = current.add(x, y);
                        if (!exluded.contains(t) && this.getDist(t, end) > -1.0D && this.getDist(t, end) < bestDistance) {
                            bestDistance = this.getDist(t, end);
                            best = t;
                        }
                    }
                }
            }

            return best;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        for (int x = 0; x < this.gridSize; ++x) {
            for (int y = 0; y < this.gridSize; ++y) {
                Tile t = new Tile(x, y, 0);
                Rectangle rect = new Rectangle(t.getX() * this.size, (this.gridSize - t.getY()) * this.size, this.size, this.size);
                if (rect.contains(e.getPoint())) {
                    if (this.obstacles.contains(t)) {
                        this.obstacles.remove(t);
                    } else {
                        this.obstacles.add(t);
                    }

                    try {
                        FileWriter writer = new FileWriter(new File("./obstacles.json"));
                        (new Gson()).toJson(this.obstacles, writer);
                        writer.close();
                    } catch (IOException var7) {
                        var7.printStackTrace();
                    }

                    return;
                }
            }
        }

        if (this.obstaclesLoad.contains(e.getPoint())) {
            try {
                this.obstacles = (ArrayList) (new Gson()).fromJson(new FileReader(new File("./obstacles.json")), (new TypeToken<ArrayList<Tile>>() {
                }).getType());
            } catch (FileNotFoundException var8) {
                var8.printStackTrace();
            }

        } else {
            this.stopAnimation = !this.stopAnimation;
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
