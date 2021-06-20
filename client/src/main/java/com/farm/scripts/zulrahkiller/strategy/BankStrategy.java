package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Positions;
import com.farm.scripts.zulrahkiller.bank.BankItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class BankStrategy extends Strategy {
    public BankItem[] requiredItems;

    public BankStrategy() {
        this.requiredItems = new BankItem[]{new BankItem("Karil's leathertop", 1), new BankItem("Karil's leatherskirt", 1), new BankItem("Ava's accumulator", 1), new BankItem("Toxic blowpipe", 1), new BankItem("Prayer potion(4)", 2), new BankItem("Anti-venom+(4)", 1), new BankItem("Ranging potion", 1), new BankItem("Magic potion", 1), new BankItem("Ring of dueling", 1), new BankItem("Ring of recoil", 1), new BankItem("Zul-andra teleport", 1), new BankItem("Manta ray", 16)};
    }

    public boolean active() {
        return true;
    }

    public void onAction() {
        if (Positions.PORTAL_LEAVE_TILE.distance() <= 30) {
            GameObjects.get("Portal").interact("Exit");
            Time.waitObjectDissapear(GameObjects.get("Portal"));
            Time.sleep(() -> Client.getRunEnergy() >= 99);
            return;
        }
        if (Positions.BANK_TILE.distance() <= 70) {
            if (Skill.HITPOINTS.getCurrentPercent() < 100 || Skill.PRAYER.getCurrentPercent() < 100 || Client.getRunEnergy() < 30) {
                if (WebWalking.walkTo(Positions.PORTAL_ENTER_TILE, 6, new Tile[0])) {
                    GameObjects.get("Free-for-all portal").interact("Enter");
                    Time.waitObjectDissapear(GameObjects.get("Free-for-all portal"));
                    Time.sleep(680, 760);
                }
            } else {
                this.doBanking();
            }
        }
    }

    private void doBanking() {
        if (!this.equipBaseItems()) {
            return;
        }
        if (this.hasItems()) {
            if (Widgets.closeTopInterface()) {
                Inventory.container().get("Zul-andra teleport").interact(ItemMethod.EAT);
                Time.sleep(() -> GameObjects.get("Sacrificial boat") != null);
            }
            return;
        }
        if (!WebWalking.walkTo(Positions.BANK_TILE, 6, new Tile[0]) || !Bank.open()) {
            return;
        }
        if (!Bank.depositAll(i -> !this.isNeeded(i))) {
            return;
        }
        for (final BankItem toWitdraw : this.requiredItems) {
            final Filter<Item> filter = (Filter<Item>) (i -> i.getName().startsWith(toWitdraw.itemName));
            if (Inventory.container().getCount((Filter) filter) < toWitdraw.amount) {
                final Item item = Bank.getContainer().get((Filter) filter);
                Bank.withdraw(item.getId(), toWitdraw.amount - Inventory.container().getCount((Filter) filter));
                Time.sleep(150, 320);
            } else if (Inventory.container().getCount((Filter) filter) > toWitdraw.amount) {
                Bank.depositAll((Filter) filter);
                Time.sleep(150, 320);
            }
        }
        Time.sleep(this::hasItems);
    }

    private boolean isNeeded(final Item item) {
        //final ArrayList<String> requiredItemList = new ArrayList<String>();
        //requiredItemList.addAll(Arrays.stream(this.requiredItems).map(i -> i.itemName).collect((Collector<? super Object, ?, Collection<? extends String>>)Collectors.toList()));
        //requiredItemList.addAll(Arrays.stream(EquipmentStrategy.MAGIC_GEAR).map(i -> i.itemName).collect((Collector<? super Object, ?, Collection<? extends String>>)Collectors.toList()));
        //requiredItemList.addAll(Arrays.stream(EquipmentStrategy.RANGED_GEAR).map(i -> i.itemName).collect((Collector<? super Object, ?, Collection<? extends String>>)Collectors.toList()));

        final ArrayList<String> requiredItemList = new ArrayList<String>();
        requiredItemList.addAll((Collection) Arrays.stream(this.requiredItems).map((i) -> {
            return i.itemName;
        }).collect(Collectors.toList()));
        requiredItemList.addAll((Collection) Arrays.stream(EquipmentStrategy.MAGIC_GEAR).map((i) -> {
            return i.itemName;
        }).collect(Collectors.toList()));
        requiredItemList.addAll((Collection) Arrays.stream(EquipmentStrategy.RANGED_GEAR).map((i) -> {
            return i.itemName;
        }).collect(Collectors.toList()));

        for (final String itemName : requiredItemList) {
            final Filter<Item> filter = (Filter<Item>) (i -> i.getName().startsWith(itemName));
            if (filter.accept((Item) item)) {
                return true;
            }
        }
        return false;
    }

    private boolean equipBaseItems() {
        for (final EquipmentStrategy.EquipmentItem item : EquipmentStrategy.BASE_GEAR) {
            final Filter<Item> filter = (Filter<Item>) (i -> i.getName().startsWith(item.itemName));
            if (Inventory.container().contains((Filter) filter)) {
                if (!Equipment.isEquipped((Filter) filter)) {
                    Widgets.closeTopInterface();
                    Inventory.container().get((Filter) filter).interact(ItemMethod.WEAR);
                    Time.sleep(300, 600);
                    return false;
                }
            } else if (!Equipment.isEquipped((Filter) filter)) {
                if (!Bank.open()) {
                    return false;
                }
                if (Inventory.getFreeSlots() < 1) {
                    Bank.depositAll();
                    return false;
                }
                Bank.withdraw(Bank.getContainer().get((Filter) filter).getId(), 1);
                Time.waitInventoryChange();
                return false;
            }
        }
        Debug.log((Object) "Jest git.");
        return true;
    }

    private boolean hasItems() {
        for (final BankItem toWitdraw : this.requiredItems) {
            final Filter<Item> filter = (Filter<Item>) (i -> i.getName().startsWith(toWitdraw.itemName));
            if (Inventory.container().getCount((Filter) filter) < toWitdraw.amount) {
                return false;
            }
        }
        return true;
    }
}
