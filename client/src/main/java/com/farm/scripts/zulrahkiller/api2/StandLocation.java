package com.farm.scripts.zulrahkiller.api2;

public enum StandLocation {
    WEST(-5, 0),
    EAST(5, -2),
    SOUTH(0, -6),
    SOUTH_WEST(-4, -4),
    SOUTH_EAST(2, -6),
    TOP_EAST(6, 2),
    TOP_WEST(-4, 3),
    PILLAR_WEST_INSIDE(-4, -3),
    PILLAR_WEST_OUTSIDE(-5, -3),
    PILLAR_EAST_INSIDE(4, -3),
    PILLAR_EAST_OUTSIDE(4, -4);

    public int x;
    public int y;

    private StandLocation(int offsetX, int offsetY) {
        this.x = 52 + offsetX;
        this.y = 59 + offsetY;
    }
}
