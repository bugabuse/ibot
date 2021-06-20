/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.game;

public class Tile {
    public int x;
    public int y;
    public int z;

    public Tile() {
    }

    public Tile(int x, int y) {
        this(x, y, 0);
    }

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}

