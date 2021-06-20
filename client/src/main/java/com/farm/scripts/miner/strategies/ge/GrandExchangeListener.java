package com.farm.scripts.miner.strategies.ge;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.miner.Miner;
import com.farm.scripts.miner.MiningUtils;
import com.farm.scripts.miner.Strategies;

public class GrandExchangeListener extends Strategy {
    public static final String[] REQUIRED_AXES = new String[]{"Iron pickaxe", "Steel pickaxe", "Mithril pickaxe", "Adamant pickaxe"};
    public static final int[] REQUIRED_LVLS = new int[]{1, 6, 21, 31};

    public boolean active() {
        return true;
    }

    public void onAction() {
        if (MiningUtils.hasPickaxe() && (Inventory.container().contains("Adamant pickaxe") || Bank.getCache().contains("Adamant pickaxe"))) {
            Strategies.muleManager.wealthToKeep = 0;
            return;
        }
        if (Bank.isOpen()) {
            int i = GrandExchangeListener.REQUIRED_AXES.length - 1;
            while (i >= 0) {
                Debug.log((Object) (Skill.MINING.getRealLevel() >= GrandExchangeListener.REQUIRED_LVLS[i]));
                if (Skill.MINING.getRealLevel() >= GrandExchangeListener.REQUIRED_LVLS[i]) {
                    final int finalI = i;
                    if (Time.sleep(2500, () -> Inventory.container().contains(GrandExchangeListener.REQUIRED_AXES[finalI])) || Time.sleep(2500, () -> Bank.getContainer().contains(GrandExchangeListener.REQUIRED_AXES[finalI]))) {
                        Debug.log((Object) "Best axe here.");
                        break;
                    }
                    Debug.log((Object) "Axe shit here.");
                    if (Bank.getContainer().getCount(new int[]{995}) + Inventory.getCount(995) >= 18000) {
                        Miner.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                        return;
                    }
                    Strategies.muleManager.activateResupplyState();
                } else {
                    --i;
                }
            }
        }
    }
}
