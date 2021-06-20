package com.farm.scripts.hunter;

import com.farm.ibot.api.wrapper.Tile;
import com.farm.scripts.hunter.trap.Trap;
import com.farm.scripts.hunter.trap.TrapSpot;
import com.farm.scripts.hunter.trap.TrapTile;

public class HunterConfig {
    public static final int[] TRAP_COUNT_LEVEL_REQUIREMENTS = new int[]{1, 20, 40, 60, 80};
    public static final TrapSpot CHINCHOMPA_SPOT_1;
    public static final TrapSpot CRISMON_SWIFT_SPOT_1;
    public static final TrapSpot WAGTAILS_SPOT_1;
    private static final Trap CHINCHOMPA_TRAP = new Trap("Box trap|Shaking box", 10008);
    private static final Trap BIRDS_TRAP = new Trap("Bird snare", 10006);
    public static TrapSpot currentSpot;

    static {
        CHINCHOMPA_SPOT_1 = new TrapSpot(new Tile[]{new TrapTile(2506, 2884, CHINCHOMPA_TRAP), new Tile(-1, -1), new Tile(-1, -3), new Tile(-3, -3), new Tile(-2, -2)});
        CRISMON_SWIFT_SPOT_1 = new TrapSpot(new Tile[]{new TrapTile(2610, 2930, BIRDS_TRAP), new Tile(-1, -1), new Tile(-2, -2), new Tile(-1, -3), new Tile(-3, -3)});
        WAGTAILS_SPOT_1 = new TrapSpot(new Tile[]{new TrapTile(2512, 2914, BIRDS_TRAP), new TrapTile(2514, 2914, BIRDS_TRAP), new TrapTile(2513, 2913, BIRDS_TRAP), new TrapTile(2514, 2912, BIRDS_TRAP), new TrapTile(2512, 2912, BIRDS_TRAP)});
        currentSpot = CRISMON_SWIFT_SPOT_1;
    }
}
