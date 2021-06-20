package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.wrapper.Tile;

public class GroundItemAction extends Action {
    private static final int xClick = 0;
    private static final int yClick = 0;

    public GroundItemAction(Tile tile, int itemId, int index) {
        super(tile.toLocalTile().getX(), tile.toLocalTile().getY(), index, itemId, "", "", 0, 0);
    }

    public static Action create(String action, GroundItem item) {
        if (item != null) {
            if (action.toLowerCase().startsWith("cast")) {

                return new GroundItemAction(item.getPosition(), item.getId(), 17);
            } else {
                return new GroundItemAction(item.getPosition(), item.getId(), 20 + ObjectAction.getIndex(item.getActions(), action));
            }
        } else {
            return new Action("GroundItemAction");
        }
    }
}
