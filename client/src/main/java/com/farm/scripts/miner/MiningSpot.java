package com.farm.scripts.miner;

import com.farm.ibot.api.wrapper.Tile;

public class MiningSpot extends Tile {
    int requiredCombat;
    int requiredMining = 1;

    public MiningSpot(int x, int y) {
        super(x, y, 0);
    }

    public MiningSpot requiredCombat(int cb) {
        this.requiredCombat = cb;
        return this;
    }

    public MiningSpot requiredMining(int mining) {
        this.requiredMining = mining;
        return this;
    }

    public MiningSpot setNote(String note) {
        super.setNote(note);
        return this;
    }
}
