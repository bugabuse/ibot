package com.farm.scripts.woodcutter.strategy.ge;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.woodcutter.ChopSettings;
import com.farm.scripts.woodcutter.Chopper;
import com.farm.scripts.woodcutter.Strategies;

public class GrandExchangeListener extends Strategy {
    public static final String[] REQUIRED_AXES = new String[]{"Bronze axe", "Iron axe", "Steel axe", "Mithril axe", "Adamant axe", "Rune axe"};
    public static final int[] REQUIRED_LVLS = new int[]{1, 1, 6, 21, 31, 41};

    public boolean active() {
        return true;
    }


    public void onAction() {
        if (!ChopSettings.powerChopping && (Inventory.container().contains("Rune axe") || Bank.getCache().contains("Rune axe") || new GrandExchangeOffer(new Item(ItemDefinition.forName("Rune axe").id, 0), 0, false).findGrandExchangeItem() != null)) {
            Strategies.muleManager.wealthToKeep = 0;
            return;
        }
        if (Bank.isOpen()) {
            int i = GrandExchangeListener.REQUIRED_AXES.length - 1;
            while (i >= 0) {
                if (Skill.WOODCUTTING.getRealLevel() >= GrandExchangeListener.REQUIRED_LVLS[i]) {
                    Debug.log((Object) ("axe check " + GrandExchangeListener.REQUIRED_AXES[i]));
                    final int finalI = i;
                    if (Time.sleep(2500, () -> Inventory.container().contains(GrandExchangeListener.REQUIRED_AXES[finalI])) || Time.sleep(2500, () -> Bank.getContainer().contains(GrandExchangeListener.REQUIRED_AXES[finalI]))) {
                        break;
                    }
                    if (Bank.getContainer().getCount(new int[]{995}) + Inventory.getCount(995) >= 4000) {
                        Debug.log((Object) ("No axe GE: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                        Debug.log((Object) ("No axe GE: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                        Debug.log((Object) ("No axe GE: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                        Debug.log((Object) ("No axe GE: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                        Debug.log((Object) ("No axe GE: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                        Debug.log((Object) ("No axe GE: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                        Chopper.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                        return;
                    }
                    Debug.log((Object) ("No axe: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                    Debug.log((Object) ("No axe: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                    Debug.log((Object) ("No axe: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                    Debug.log((Object) ("No axe: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                    Debug.log((Object) ("No axe: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                    Debug.log((Object) ("No axe: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                    Debug.log((Object) ("No axe: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                    Debug.log((Object) ("No axe: " + GrandExchangeListener.REQUIRED_AXES[finalI]));
                    Strategies.muleManager.activateResupplyState();
                } else {
                    --i;
                }
            }
        }
    }
}
