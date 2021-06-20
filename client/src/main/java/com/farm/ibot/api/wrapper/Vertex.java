package com.farm.ibot.api.wrapper;

import com.farm.ibot.api.world.Screen;

public class Vertex {
    public int x;
    public int y;
    public int z;

    public Vertex(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex rotate(int orientation) {
        orientation = (orientation + 1024) % 2048;
        if (orientation == 0) {
            return this;
        } else {
            int sin = Screen.SINE[orientation];
            int cos = Screen.COSINE[orientation];
            return new Vertex(this.x * cos + this.z * sin >> 16, this.y, this.z * cos - this.x * sin >> 16);
        }
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
