package com.farm.scripts.claysoftener.strategies;

import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.InputBox.MakeItemDialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.claysoftener.Constants;

import java.awt.*;

public class SoftenClayStrategy extends Strategy implements InventoryListener {
    private static PaintTimer softenTimer = new PaintTimer();

    public SoftenClayStrategy(BotScript script) {
        script.addEventHandler(new InventoryEventHandler(this));
    }

    public static boolean isSoftening() {
        return Inventory.get(1929) != null && Inventory.get(434) != null && softenTimer.getElapsed() < 5000L;
    }

    public boolean active() {
        return Constants.currentState == 0 && !BucketFillStrategy.isFilling() && Inventory.container().getCount(new int[]{1925, 1929}) >= 14 && Inventory.get(1929) != null && Inventory.get(434) != null;
    }

    public void onAction() {

        if (Widgets.closeTopInterface()) {
            if (Inventory.get(1929) != null && Inventory.get(434) != null) {
                if (!isSoftening() && (InputBox.isMakeItemDialogueOpen() || Inventory.get(1929).interactWith(Inventory.get(434))) && Time.sleep(InputBox::isMakeItemDialogueOpen)) {

                    MakeItemDialogue.MAKE_ALL.selectAndExecute();
                    Time.sleep(SoftenClayStrategy::isSoftening);
                }

            }
        }
    }

    private boolean isDialogueOpened() {
        return Screen.colorMatches(new Color(81, 56, 5), 20, 258, 414);
    }

    public void onItemAdded(Item item) {
        if (item.getId() == 1761) {
            softenTimer.reset();
        }

    }
}
