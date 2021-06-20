package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.PlayerComposite;

public interface IPlayer {
    int getDestinationX(Object var1);

    int getDestinationY(Object var1);

    int getCombatLevel(Object var1);

    String getName(Object var1);

    boolean isVisible(Object var1);

    PlayerComposite getComposite(Object var1);

    Player getLocal(Object var1);

    Player[] getPlayers(Object var1);
}
