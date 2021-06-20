package com.farm.scripts.zulrahkiller.api;

import com.farm.ibot.api.wrapper.Tile;
import com.farm.scripts.zulrahkiller.api2.StandLocation;

public enum Pattern {
    NONE(new ZulrahRotation[0]),
    PATTERN_1(new ZulrahRotation[]{green(Positions.RIGHT_CORNER).waitBeforeWalk(2400), red(Positions.RIGHT_CORNER).withAlternate(Positions.RIGHT_CORNER_ALTERNATE), blue(Positions.RIGHT_CORNER), new ZulrahRotation(Zulrah.Color.GREEN, Positions.LEFT_COLUMN_CLOSE, Positions.ZULRAH_BOTTOM), red(Positions.LEFT_COLUMN_CLOSE), blue(Positions.CENTER_BOTTOM).withAllowedDistance(5), green(Positions.RIGHT_COLUMN), blue(Positions.LEFT_COLUMN_CLOSE).waitBeforeWalk(7800), green(Positions.LEFT_CORNER_ALTERNATE).withJadMode(true), red(Positions.LEFT_CORNER_ALTERNATE).withAlternate(Positions.LEFT_CORNER)}),
    PATTERN_2(new ZulrahRotation[]{green(Positions.RIGHT_CORNER).waitBeforeWalk(2400), red(Positions.RIGHT_CORNER).withAlternate(Positions.RIGHT_CORNER_ALTERNATE), blue(Positions.RIGHT_CORNER), new ZulrahRotation(Zulrah.Color.GREEN, Positions.LEFT_CORNER, Positions.ZULRAH_LEFT), blue(Positions.LEFT_COLUMN_CLOSE), red(Positions.LEFT_COLUMN_CLOSE), green(Positions.CENTER_BOTTOM).withAllowedDistance(5), blue(Positions.LEFT_COLUMN), green(Positions.LEFT_CORNER_ALTERNATE).withJadMode(true), red(Positions.LEFT_CORNER_ALTERNATE).withAlternate(Positions.LEFT_CORNER)}),
    PATTERN_2_EXTENDED(new ZulrahRotation[]{green(Positions.RIGHT_CORNER).waitBeforeWalk(2400), red(Positions.RIGHT_CORNER).withAlternate(Positions.RIGHT_CORNER_ALTERNATE), blue(Positions.RIGHT_CORNER), new ZulrahRotation(Zulrah.Color.GREEN, Positions.LEFT_CORNER, Positions.ZULRAH_LEFT), (new ZulrahRotation(Zulrah.Color.GREEN, Positions.LEFT_COLUMN, Positions.ZULRAH_RIGHT)).setIdleMode(true), blue(Positions.LEFT_COLUMN_CLOSE), red(Positions.LEFT_COLUMN), green(Positions.CENTER_BOTTOM).withAllowedDistance(5), blue(Positions.LEFT_COLUMN), green(Positions.LEFT_CORNER_ALTERNATE).withJadMode(true), red(Positions.LEFT_CORNER_ALTERNATE).withAlternate(Positions.LEFT_CORNER)}),
    PATTERN_3(new ZulrahRotation[]{green(Positions.RIGHT_CORNER).waitBeforeWalk(2400), new ZulrahRotation(Zulrah.Color.GREEN, Positions.RIGHT_CORNER, Positions.ZULRAH_RIGHT), red(Positions.RIGHT_COLUMN), blue(Positions.CENTER_BOTTOM).withAllowedDistance(7), green(Positions.CENTER_BOTTOM), blue(Positions.CENTER_BOTTOM).withAllowedDistance(7), green(Positions.LEFT_COLUMN), green(Positions.LEFT_COLUMN), blue(Positions.RIGHT_COLUMN), green(Positions.RIGHT_COLUMN).withJadMode(false), blue(Positions.RIGHT_COLUMN)}),
    PATTERN_4(new ZulrahRotation[]{green(Positions.RIGHT_CORNER).waitBeforeWalk(2400), blue(Positions.RIGHT_CORNER), green(Positions.LEFT_COLUMN_CLOSE), blue(Positions.LEFT_COLUMN_CLOSE), red(Positions.RIGHT_COLUMN), new ZulrahRotation(Zulrah.Color.GREEN, Positions.RIGHT_COLUMN, Positions.ZULRAH_RIGHT), (new ZulrahRotation(Zulrah.Color.GREEN, Positions.LEFT_COLUMN, Positions.ZULRAH_CENTER)).waitBeforeWalk(7800), blue(Positions.LEFT_COLUMN), green(Positions.RIGHT_COLUMN).waitBeforeWalk(2400), blue(Positions.RIGHT_COLUMN), green(Positions.RIGHT_COLUMN).withJadMode(false), blue(Positions.RIGHT_CORNER)});

    ZulrahRotation[] strategies;

    private Pattern(ZulrahRotation... strategies) {
        this.strategies = strategies;
    }

    private static ZulrahRotation red(Tile tile) {
        return new ZulrahRotation(Zulrah.Color.RED, tile);
    }

    private static ZulrahRotation green(Tile tile) {
        return new ZulrahRotation(Zulrah.Color.GREEN, tile);
    }

    private static ZulrahRotation blue(Tile tile) {
        return new ZulrahRotation(Zulrah.Color.BLUE, tile);
    }

    private static ZulrahRotation red(StandLocation tile) {
        return new ZulrahRotation(Zulrah.Color.RED, new Tile(tile.x, tile.y));
    }

    private static ZulrahRotation green(StandLocation tile) {
        return new ZulrahRotation(Zulrah.Color.GREEN, new Tile(tile.x, tile.y));
    }

    private static ZulrahRotation blue(StandLocation tile) {
        return new ZulrahRotation(Zulrah.Color.BLUE, new Tile(tile.x, tile.y));
    }
}
