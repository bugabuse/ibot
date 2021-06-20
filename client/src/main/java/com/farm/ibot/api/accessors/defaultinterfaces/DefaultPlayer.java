package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.PlayerComposite;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IPlayer;

public class DefaultPlayer implements IPlayer {
    public int getDestinationX(Object instance) {
        return (Integer) Wrapper.getStatic("Player.DestinationX");
    }

    public int getDestinationY(Object instance) {
        return (Integer) Wrapper.getStatic("Player.DestinationY");
    }

    public int getCombatLevel(Object instance) {
        return (Integer) Wrapper.get("Player.CombatLevel", instance);
    }

    public String getName(Object instance) {
        return "" + Wrapper.get("Player.Name", instance);
    }

    public boolean isVisible(Object instance) {
        return (Boolean) Wrapper.get("Player.Visible", instance);
    }

    public PlayerComposite getComposite(Object instance) {
        return (PlayerComposite) Wrapper.get("Player.Composite", PlayerComposite.class, instance);
    }

    public Player getLocal(Object instance) {
        return (Player) Wrapper.getStatic("Player.Local", Player.class);
    }

    public Player[] getPlayers(Object instance) {
        return (Player[]) Wrapper.getStatic("Player.Players", Player[].class);
    }
}
