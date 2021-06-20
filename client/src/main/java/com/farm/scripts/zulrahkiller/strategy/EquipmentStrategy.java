package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Equipment.Slot;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Prayer;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Zulrah;

import java.util.Arrays;

public class EquipmentStrategy extends Strategy implements MessageListener {
    public static final EquipmentStrategy.EquipmentItem[] RANGED_GEAR;
    public static final EquipmentStrategy.EquipmentItem[] MAGIC_GEAR;
    public static final EquipmentStrategy.EquipmentItem[] BASE_GEAR;

    static {
        RANGED_GEAR = new EquipmentStrategy.EquipmentItem[]{new EquipmentStrategy.EquipmentItem("Karil's leathertop", Slot.LEGS), new EquipmentStrategy.EquipmentItem("Karil's leatherskirt", Slot.CHEST), new EquipmentStrategy.EquipmentItem("Ava's accumulator", Slot.CAPE), new EquipmentStrategy.EquipmentItem("Toxic blowpipe", Slot.SWORD)};
        MAGIC_GEAR = new EquipmentStrategy.EquipmentItem[]{new EquipmentStrategy.EquipmentItem("Ahrim's robetop", Slot.LEGS), new EquipmentStrategy.EquipmentItem("Ahrim's robeskirt", Slot.CHEST), new EquipmentStrategy.EquipmentItem("Trident of the swamp", Slot.SWORD), new EquipmentStrategy.EquipmentItem("Imbued saradomin cape", Slot.CAPE), new EquipmentStrategy.EquipmentItem("Malediction ward", Slot.SHIELD)};
        BASE_GEAR = new EquipmentStrategy.EquipmentItem[]{new EquipmentStrategy.EquipmentItem("Combat bracelet", Slot.GLOVES), new EquipmentStrategy.EquipmentItem("Ring of recoil", Slot.RING), new EquipmentStrategy.EquipmentItem("Wizard boots", Slot.BOOTS), new EquipmentStrategy.EquipmentItem("Occult necklace", Slot.AMULET), new EquipmentStrategy.EquipmentItem("Imbued saradomin cape", Slot.CAPE), new EquipmentStrategy.EquipmentItem("Ahrim's hood", Slot.HELM), new EquipmentStrategy.EquipmentItem("Ahrim's robetop", Slot.LEGS), new EquipmentStrategy.EquipmentItem("Ahrim's robeskirt", Slot.CHEST), new EquipmentStrategy.EquipmentItem("Trident of the swamp", Slot.SWORD), new EquipmentStrategy.EquipmentItem("Malediction ward", Slot.SHIELD)};
    }

    private boolean hasRecoilRing = false;

    public EquipmentStrategy(BotScript script) {
        script.addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Zulrah.Color color = null;
        if (Zulrah.getNextRotationStrategy() == null || Zulrah.get() != null && !Zulrah.isDying()) {
            color = Zulrah.getColor();
        } else {
            color = Zulrah.getNextRotationStrategy().getColor();
        }

        Item recoilRing = Inventory.get("Ring of recoil");
        if (!this.hasRecoilRing && recoilRing != null) {

            if (recoilRing.interact(ItemMethod.WEAR)) {
                this.hasRecoilRing = Time.sleep(2000, () -> {
                    return Equipment.isEquipped(2550);
                });
            }
        }

        if (color != null) {
            switch (color) {
                case RED:
                    this.equip(MAGIC_GEAR);
                    break;
                case BLUE:
                    this.equip(RANGED_GEAR);
                    break;
                case GREEN:
                    this.equip(MAGIC_GEAR);
            }
        } else {
            Arrays.stream(Prayer.values()).forEach((p) -> {
                p.setEnabled(false);
            });
        }

    }

    private void equip(EquipmentStrategy.EquipmentItem... items) {
        EquipmentStrategy.EquipmentItem[] var2 = items;
        int var3 = items.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            EquipmentStrategy.EquipmentItem item = var2[var4];
            Filter<Item> filter = (i) -> {
                return i.getName().startsWith(item.itemName);
            };
            Item inventoryItem = Inventory.container().get(filter);
            if (!Equipment.isEquipped(item.slot, filter) && inventoryItem != null && inventoryItem.interact(ItemMethod.WEAR)) {
                Time.sleep(30, 160);
            }
        }

        this.sleep(1200, 1400);
    }

    public void onMessage(String message) {
        if (message.contains("shattered")) {
            this.hasRecoilRing = false;
        }

        if (message.contains("you are dead!")) {
            Debug.log("Death at pattern: " + Zulrah.getPattern());
        }

    }

    static class EquipmentItem {
        public String itemName;
        public Slot slot;

        public EquipmentItem(String itemName, Slot slot) {
            this.itemName = itemName;
            this.slot = slot;
        }
    }
}
