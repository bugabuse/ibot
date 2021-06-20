package com.farm.scripts.quester.quests.sheepshearer.strategy;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.InputBox.MakeItemDialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.sheepshearer.SheepState;

public class ShearSheep extends Strategy {
    public static final int BALL_OF_WOOL = 1759;
    private static final int WOOL = 1737;
    private static final int SHEARS = 1735;
    public static Tile SHEEP_TILE = new Tile(3199, 3271, 0);
    public static Tile SPIN_TILE = new Tile(3209, 3213, 1);
    private static long lastAnim = 0L;

    public static boolean isSpinning() {
        if (Player.getLocal().getAnimation() != -1) {
            lastAnim = System.currentTimeMillis();
        }

        return System.currentTimeMillis() - lastAnim < 5500L;
    }

    public boolean active() {
        return !SheepState.isInState(SheepState.FINISHING_QUEST) && !Inventory.contains(1759, 20);
    }

    public void onAction() {
        if (Inventory.container().getCount(new int[]{1737, 1759}) < 20) {
            if (Inventory.container().getCount(new int[]{1737, 1759}) + Inventory.getFreeSlots() < 20) {
                if (WebWalking.walkTo(Locations.getClosestBank(), new Tile[0]) && Bank.open()) {
                    Bank.depositAll();
                }

            } else {
                this.shear();
            }
        } else if (Inventory.container().getCount(new int[]{1737, 1759}) >= 20) {
            this.makeBallOfWool();
        }
    }

    private void makeBallOfWool() {
        if (WebWalking.walkTo(SPIN_TILE, 1, new Tile[0]) && !isSpinning()) {
            if (!InputBox.isMakeItemDialogueOpen()) {
                if (GameObjects.get("Spinning wheel").interact("Spin")) {
                    Time.sleep(() -> {
                        return InputBox.isMakeItemDialogueOpen();
                    });
                }
            } else {
                MakeItemDialogue.MAKE_ALL.selectAndExecute();
                Time.waitInventoryChange();
            }
        }

    }

    private void shear() {
        if (!Inventory.contains(1735)) {
            if (WebWalking.walkTo(FarmerTalk.FARMER_TILE, 5, new Tile[0])) {
                GroundItem item = GroundItems.get(1735);
                if (item != null) {
                    item.interact("Take");
                    Time.waitInventoryChange();
                }
            }

        } else if (!SHEEP_TILE.isReachable()) {
            WebWalking.walkTo(SHEEP_TILE, new Tile[0]);
        } else {
            Npc sheep = Npcs.get((npc) -> {
                return npc.getName().startsWith("Sheep") && StringUtils.containsAny("Shear", npc.getActions()) && !StringUtils.containsAny("Talk-to", npc.getActions()) && npc.isReachable();
            });
            if (sheep != null && sheep.interact("Shear")) {
                if (Time.sleep(3000, () -> {
                    return Player.getLocal().getAnimation() != -1;
                })) {
                    Time.waitInventoryChange();
                } else {
                    Time.sleep(() -> {
                        return Player.getLocal().getAnimation() == -1;
                    });
                }
            }

        }
    }
}
