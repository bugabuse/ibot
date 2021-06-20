package com.farm.scripts.tab.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interact.action.impl.ObjectAction;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.tab.Strategies;

public class MakeTabStrategy extends Strategy {
    private static long lastAnim = 0L;

    public static boolean isTabbing() {
        if (Player.getLocal().getAnimation() == 4068) {
            lastAnim = System.currentTimeMillis();
        }

        return System.currentTimeMillis() - lastAnim < 5500L;
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Client.getCurrentWorld() != 330) {
            WorldHopping.hop(330);
        } else if (Inventory.contains(1761)) {
            if (!UnnoteStrategy.isAtHome()) {
                if (WebWalking.walkTo(new Tile(2954, 3224, 0), new Tile[0])) {
                    if (HouseOwnersSearcher.currentHouse != null) {
                        if (InputBox.isOpen()) {
                            InputBox.input(HouseOwnersSearcher.currentHouse);
                            if (Time.sleep(UnnoteStrategy::isAtHome)) {
                                Time.sleep(1500, 3000);
                            }

                        } else {
                            ObjectAction.create(3, GameObjects.get("Portal")).sendByMouse();
                            Time.sleep(InputBox::isOpen);
                        }
                    }
                }
            } else {
                if (!isTabbing()) {
                    if (Widgets.get(79, Strategies.getTabToMake()) != null) {
                        Widget widget = Widgets.get(79, Strategies.getTabToMake());
                        if (widget != null) {

                            widget.interact("Make-All");
                            Time.sleep(1000, 3000);
                            return;
                        }

                        return;
                    }

                    if (GameObjects.get(13647) != null) {
                        GameObjects.get(13647).interact("Study");
                        Time.sleep(() -> {
                            return Widgets.get(79, Strategies.getTabToMake()) != null;
                        });
                    } else if (!Time.sleep(10000, () -> {
                        return GameObjects.get(13647) != null;
                    })) {
                        HouseOwnersSearcher.banCurrentHouse();
                        GameObjects.get("Portal").interact("Enter");
                    }
                }

            }
        }
    }
}
