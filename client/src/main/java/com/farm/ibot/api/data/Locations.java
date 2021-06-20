package com.farm.ibot.api.data;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.util.Sorting;
import com.farm.ibot.api.wrapper.Tile;

public class Locations {
    public static final Tile RESUPPLY_REGULAR_TILE = new Tile(3162, 3488, 0);
    public static final Tile GRAND_EXCHANGE = new Tile(3162, 3488, 0);
    public static final Tile BANK_VARROCK_VEST = new Tile(3182, 3441, 0);
    public static final Tile BANK_VARROCK_EAST = new Tile(3254, 3420, 0);
    public static final Tile BANK_LUMBRIDGE = new Tile(3209, 3218, 2);
    public static final Tile BANK_ZEAH = new Tile(1676, 3562, 0);
    public static final Tile BANK_EDGEVILLE = new Tile(3093, 3491, 0);
    public static final Tile BANK_FALADOR_EAST = new Tile(3013, 3356, 0);
    public static final Tile BANK_FALADOR_WEST = new Tile(2947, 3371, 0);
    public static final Tile BANK_RIMMINGTON = new Tile(3093, 3242, 0);
    public static final Tile BANK_AL_KHARID = new Tile(3270, 3167, 0);
    public static final Tile BANK_CLAN_WARS = new Tile(3369, 3170, 0);
    public static final Tile BANK_CAMELOT = new Tile(2725, 3491, 0);
    public static final Tile BANK_CASTLE_WARS = new Tile(2443, 3083, 0);
    public static final Tile DEPOSIT_PORT_SARIM = new Tile(3045, 3235, 0);
    public static final Tile[] BANKS;
    public static final Tile[] DEPOSIT_BOXES;
    public static final Tile TELEPORT_FALADOR;
    public static final Tile TELEPORT_CAMELOT;

    static {
        BANKS = new Tile[]{GRAND_EXCHANGE, BANK_VARROCK_EAST, BANK_VARROCK_VEST, BANK_LUMBRIDGE, BANK_ZEAH, BANK_EDGEVILLE, BANK_FALADOR_EAST, BANK_RIMMINGTON, BANK_AL_KHARID, BANK_FALADOR_WEST, BANK_CLAN_WARS, BANK_CAMELOT, BANK_CASTLE_WARS};
        DEPOSIT_BOXES = new Tile[]{DEPOSIT_PORT_SARIM};
        TELEPORT_FALADOR = new Tile(2965, 3377, 0);
        TELEPORT_CAMELOT = new Tile(2756, 3479, 0);
    }

    public static Tile getClosestBank(Tile tile) {
        return Sorting.getNearest(tile, BANKS);
    }

    public static Tile getClosestBank() {
        return getClosestBank(Player.getLocal().getPosition());
    }

    public static Tile getClosestDepositBox(Tile tile) {
        return Sorting.getNearest(tile, DEPOSIT_BOXES);
    }

    public static Tile getClosestDepositBox() {
        return getClosestDepositBox(Player.getLocal().getPosition());
    }
}
