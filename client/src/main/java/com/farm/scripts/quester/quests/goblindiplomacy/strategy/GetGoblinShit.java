package com.farm.scripts.quester.quests.goblindiplomacy.strategy;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.goblindiplomacy.GoblinDiplomacy;
import com.farm.scripts.quester.quests.goblindiplomacy.GoblinState;

public class GetGoblinShit extends Strategy {
    public static final int ID_GOBLIN_MAIL_BROWN = 288;
    public static final int ID_GOBLIN_MAIL_BLUE = 287;
    public static final int ID_GOBLIN_MAIL_ORANGE = 286;
    public static final int ID_BLUE_DYE = 1767;
    public static final int ID_ORANGE_DYE = 1769;

    public void onAction() {
        if (Inventory.getFreeSlots() >= 5 && !Inventory.container().contains(289)) {
            if (GoblinState.isInState(GoblinState.SHOW_ORANGE_ARMOUR, GoblinState.SHOW_BLUE_ARMOUR, GoblinState.SHOW_BROWN_ARMOUR) || this.gatherItems()) {

                this.doTalking();
            }
        } else {
            if (WebWalking.walkTo(Locations.getClosestBank(), new Tile[0]) && Bank.open()) {
                Bank.depositAll();
            }

        }
    }

    private boolean gatherItems() {
        if (!Inventory.container().containsAll(new int[]{288, 287, 286})) {
            if (!Bank.hasCache()) {
                Bank.openNearest();
                return false;
            } else {
                Item[] toWithdraw = new Item[]{new Item(288, 3), new Item(287, 1), new Item(286, 1), new Item(1767, 1), new Item(1769, 1)};
                Item[] var2 = toWithdraw;
                int var3 = toWithdraw.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    Item item = var2[var4];
                    if (!Inventory.contains(item.getId(), item.getAmount()) && Bank.getCache().contains(item.getId(), item.getAmount())) {
                        Bank.openAndWithdraw(new Item[]{item});
                        return false;
                    }
                }

                if (Inventory.container().contains(288) && Inventory.container().containsAny(new int[]{1767, 1769})) {
                    Widgets.closeTopInterface();
                    Inventory.container().getAny(new int[]{1767, 1769}).interactWith(Inventory.container().get(288));
                    Time.waitInventoryChange();
                    return false;
                } else {
                    GoblinDiplomacy.instance.setCurrentlyExecutitng(GoblinDiplomacy.GRAND_EXCHANGE);

                    return false;
                }
            }
        } else {
            return true;
        }
    }

    private void doTalking() {
        if (Dialogue.isInTheCutScene()) {
            Dialogue.goNext(new String[0]);
        } else if (WebWalking.walkTo(new Tile(2957, 3513, 0), 8, new Tile[0])) {
            Npc npc = Npcs.get("General wartface");
            switch (GoblinState.getState()) {
                case SHOW_ORANGE_ARMOUR:
                    if (!Dialogue.isInDialouge()) {
                        Inventory.get(286).interactWith(npc);
                        Time.sleep(Dialogue::isInDialouge);
                    }
                    break;
                case SHOW_BLUE_ARMOUR:
                    if (!Dialogue.isInDialouge()) {
                        Inventory.get(287).interactWith(npc);
                        Time.sleep(Dialogue::isInDialouge);
                    }
                    break;
                case SHOW_BROWN_ARMOUR:
                    if (!Dialogue.isInDialouge()) {
                        Inventory.get(288).interactWith(npc);
                        Time.sleep(Dialogue::isInDialouge);
                    }
            }

            if (Dialogue.talkTo(npc, true)) {
                Dialogue.goNext(new String[]{"Do you want me", "different colour", "doesn't look fat", "armour here"});
            }

        }
    }
}
