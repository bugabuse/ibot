package com.farm.ibot.api.world.webwalking.node;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.webwalking.requirement.InventoryItemsRequirement;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;

public class TeleportWebNode extends WebNode {
    private int itemId;

    public TeleportWebNode() {
        this.type = WebNodeType.TELEPORT;
    }

    public TeleportWebNode(Tile position, WebNode parent, int teletabItemId) {
        super(position, parent);
        this.type = WebNodeType.TELEPORT;
        this.itemId = teletabItemId;
        this.requirements.add(new InventoryItemsRequirement(new Item[]{new Item(this.itemId, 1)}));
    }

    public boolean traverse(WebNode nextNode, boolean randomize) {
        Item item = Inventory.get(this.itemId);
        if (item != null) {
            item.interact("Break");
            Time.waitInventoryChange();
            Time.sleep(3000, 5000);
            return true;
        } else {
            return false;
        }
    }

    public boolean isReachable() {
        return true;
    }
}
