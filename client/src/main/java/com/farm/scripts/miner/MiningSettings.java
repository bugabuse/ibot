package com.farm.scripts.miner;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.init.AccountData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MiningSettings {
    public static int[] rocksIds = new int[]{7454, 7487, 7455, 7488};
    public static MiningSpot[] miningTiles = new MiningSpot[]{(new MiningSpot(3183, 3377)).requiredCombat(1).setNote("Varrock clay 1"), (new MiningSpot(2987, 3240)).requiredCombat(1).setNote("Rimington clay 1"), (new MiningSpot(2986, 3239)).requiredCombat(1).setNote("Rimington clay 2"), (new MiningSpot(3180, 3372)).requiredCombat(13).setNote("Varrock clay 2"), (new MiningSpot(3179, 3371)).requiredCombat(13).setNote("Varrock clay 3")};

    public static MiningSpot getSpot() {
        MiningSpot[] tiles = getAvailableTiles();
        return tiles[MathUtils.clamp(AccountData.current().getUniqueScriptId() % tiles.length, 0, tiles.length)];
    }

    public static MiningSpot[] getAvailableTiles() {
        List<MiningSpot> available = (List) Arrays.stream(miningTiles).filter((s) -> {
            return Skill.MINING.getRealLevel() >= s.requiredMining;
        }).collect(Collectors.toList());
        return (MiningSpot[]) available.toArray(new MiningSpot[available.size()]);
    }
}
