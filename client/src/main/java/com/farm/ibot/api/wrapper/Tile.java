package com.farm.ibot.api.wrapper;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.Animable;
import com.farm.ibot.api.interfaces.Positionable;
import com.farm.ibot.api.methods.TileReach;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.world.pathfinding.impl.LocalPathFinder;

import java.awt.*;

public class Tile implements Animable, Positionable {
    public transient int modelHeight;
    private String note;
    private int x;
    private int y;
    private int z;
    private int hashcode;

    public Tile() {
        this.note = "";
        this.modelHeight = 0;
    }

    public Tile(int x, int y) {
        this(x, y, Client.getPlane());
    }

    public Tile(int x, int y, int z) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        int hash = x * 105 + y;
        hash = hash * 105 + z;
        this.hashcode = hash;
    }

    public static int realDistance(Tile pos1, Tile pos2) {
        if (pos1 != null && pos2 != null) {
            return pos1.getZ() != pos2.getZ() ? 15002 : pos1.realDistance(pos2);
        } else {
            return 15001;
        }
    }

    public static int distance(Tile pos1, Tile pos2) {
        if (pos1 != null && pos2 != null) {
            return pos1.getZ() != pos2.getZ() ? 15002 : pos1.distance(pos2);
        } else {
            return 15001;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getModelHeight() {
        return this.modelHeight;
    }

    public Tile setModelHeight(int modelHeight) {
        this.modelHeight = modelHeight;
        return this;
    }

    public Tile toAnimable() {
        if (this.isLocal()) {
            return new Tile(this.x * 128, this.y * 128, this.z);
        } else {
            return this.isWorld() ? this.toLocalTile().toAnimable() : this;
        }
    }

    public Tile getAnimablePosition() {
        return this.toAnimable();
    }

    public Tile toWorldTile() {
        if (this.isLocal()) {
            return new Tile(Client.getBaseX() + this.x, Client.getBaseY() + this.y, this.z);
        } else {
            return this.isAnimable() ? new Tile(Client.getBaseX() + this.x / 128, Client.getBaseY() + this.y / 128, this.z) : this;
        }
    }

    public Tile toLocalTile() {
        if (this.isLocal()) {
            return this;
        } else {
            return this.isAnimable() ? this.toWorldTile().toLocalTile() : new Tile(this.x - Client.getBaseX(), this.y - Client.getBaseY(), this.z);
        }
    }

    private boolean isAnimable() {
        return this.x < Client.getBaseX() || this.x > Client.getBaseX() + 127 || this.y < Client.getBaseY() || this.y > Client.getBaseY() + 127;
    }

    private boolean isLocal() {
        return this.x <= 127 && this.y <= 127;
    }

    private boolean isWorld() {
        return !this.isAnimable() && !this.isLocal();
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }

    public int animableDistance() {
        return this.animableDistance(Player.getLocal().getAnimablePosition());
    }

    public int animableDistance(Tile tile) {
        return tile != null ? this.toAnimable().distance(tile.toAnimable()) : 99999;
    }

    public String getNote() {
        return this.note;
    }

    public Tile setNote(String note) {
        this.note = note;
        return this;
    }

    public int distance() {
        return this.distance(Player.getLocal().getPosition());
    }

    public int distance(Tile pos) {
        return pos != null ? (int) (Math.sqrt(Math.pow((double) (pos.x - this.x), 2.0D) + Math.pow((double) (pos.y - this.y), 2.0D)) + (double) (pos.getZ() == this.getZ() ? 0 : 50)) : 999999;
    }

    public double distanceD(Tile pos) {
        return pos != null ? Math.sqrt(Math.pow((double) (pos.x - this.x), 2.0D) + Math.pow((double) (pos.y - this.y), 2.0D)) + (double) (pos.getZ() == this.getZ() ? 0 : 50) : 999999.0D;
    }

    public boolean isOnRegion() {
        Tile local = new Tile(this.x - Client.getBaseX(), this.y - Client.getBaseY(), this.z);
        return local.getX() > 0 && local.getY() > 0 && local.getX() < 128 && local.getY() < 128;
    }

    public int realDistance() {
        return this.realDistance(Player.getLocal().getPosition());
    }

    public int realDistance(Tile pos, boolean ignoreObjects) {
        Tile[] path = (new LocalPathFinder(ignoreObjects)).findPath(pos, this);
        Tile[] path2 = (new LocalPathFinder(ignoreObjects)).findPath(this, pos);
        return path != null && path2 != null ? path.length - 1 : 15000;
    }

    public int realDistance(Tile pos) {
        return this.realDistance(pos, false);
    }

    public boolean isReachable() {
        return Player.getLocal().getPosition().getZ() == this.z && this.distance() <= 30 && TileReach.isReachable(Player.getLocal().getPosition(), this);
    }

    public int getAnimableX() {
        return this.toAnimable().getX();
    }

    public int getAnimableY() {
        return this.toAnimable().getY();
    }

    public Tile getPosition() {
        return this;
    }

    public Point getScreenPoint() {
        return Screen.worldToScreen(this.getAnimableX(), this.getAnimableY(), this.getModelHeight());
    }

    public Polygon getBounds() {
        Polygon polygon = new Polygon();
        Point pn = (new Tile(this.x, this.y)).setModelHeight(this.modelHeight).getScreenPoint();
        Point px = (new Tile(this.x + 1, this.y)).setModelHeight(this.modelHeight).getScreenPoint();
        Point py = (new Tile(this.x, this.y + 1)).setModelHeight(this.modelHeight).getScreenPoint();
        Point pxy = (new Tile(this.x + 1, this.y + 1)).setModelHeight(this.modelHeight).getScreenPoint();
        polygon.addPoint(py.x, py.y);
        polygon.addPoint(pxy.x, pxy.y);
        polygon.addPoint(px.x, px.y);
        polygon.addPoint(pn.x, pn.y);
        return polygon;
    }

    public boolean equals(Object other) {
        return this.hashCode() == other.hashCode();
    }

    public int hashCode() {
        return this.hashcode;
    }

    public boolean isOnMinimap() {
        return Screen.isOnMinimap(this);
    }

    public Point getMinimapPoint() {
        return Screen.tileToMap(this);
    }

    public Tile add(int x, int y) {
        return new Tile(this.x + x, this.y + y, this.getZ());
    }

    public Tile randomize(int radius) {
        return new Tile(this.x + Random.next(-radius, radius), this.y + Random.next(-radius, radius), this.z);
    }
}
