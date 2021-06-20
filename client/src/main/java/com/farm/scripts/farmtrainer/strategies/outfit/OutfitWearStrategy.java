package com.farm.scripts.farmtrainer.strategies.outfit;

import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.web.account.AccountConfig;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.scripts.farmtrainer.FarmingTrainer;
import com.farm.scripts.farmtrainer.Strategies;

public class OutfitWearStrategy extends Strategy {
    public static final int[][] OUTFITS = new int[][]{{12205, 12207, 2914, 2922, 1040, 1731, 2466}, {10408, 10410, 2464, 1055, 1837, 1727, 2902}, {10414, 10412, 3791, 3799, 1729, 1053}, {10418, 10416, 2934, 2942, 11978, 2643}, {10404, 1725, 10406, 2904, 2912, 2462, 1007, 1057}, {1046, 1704, 2936, 2938, 2934, 2942, 4331, 1323}, {2643, 10402, 10400, 632, 2902, 1019, 2643}, {2900, 1731, 2896, 2898, 2894, 2902, 1323, 3779}};
    public AccountConfig config;
    private StrategyContainer geStrategies;

    public OutfitWearStrategy(StrategyContainer geStrategies, AccountConfig config) {
        this.geStrategies = geStrategies;
        this.config = config;
    }

    public OutfitWearStrategy() {
        this.geStrategies = Strategies.GRAND_EXCHANGE_OUTFIT;
        this.config = FarmingTrainer.config;
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (!this.config.containsKey("Outfit")) {
            this.config.put("Outfit", Random.next(0, OUTFITS.length));
            this.config.update();
        }

        int[] ourOutfit = OUTFITS[this.config.getInt("Outfit")];
        int[] var2 = ourOutfit;
        int var3 = ourOutfit.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            int itemId = var2[var4];
            if (!Equipment.isEquippedIgnoreAmmo(itemId)) {

                ScriptUtils.interruptCurrentLoop();
                if (Inventory.contains(itemId)) {
                    Widgets.closeTopInterface();
                    Inventory.get(itemId).interact(ItemMethod.WEAR);
                } else if (Bank.getCache().contains(itemId)) {
                    Bank.openAndWithdraw(new Item[]{new Item(itemId, 1)});
                } else {
                    MultipleStrategyScript.get().setCurrentlyExecutitng(this.geStrategies);
                }
            }
        }

    }
}
