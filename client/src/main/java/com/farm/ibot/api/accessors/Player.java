package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IPlayer;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;

public class Player extends Character implements Interactable {
    public Player(Object instance) {
        super(instance);
    }

    public static IPlayer getPlayerInterface() {
        return Bot.get().accessorInterface.playerInterface;
    }

    @HookName("Player.DestinationX")
    public static int getDestinationX() {
        return getPlayerInterface().getDestinationX(null);
    }

    @HookName("Player.DestinationY")
    public static int getDestinationY() {
        return getPlayerInterface().getDestinationY(null);
    }

    @HookName("Player.Local")
    public static Player getLocal() {
        return getPlayerInterface().getLocal(null);
    }

    @HookName("Player.Players")
    public static Player[] getPlayers() {
        return getPlayerInterface().getPlayers(null);
    }

    public static Tile getDestination() {
        return !getLocal().isMoving() || getDestinationX() <= 0 && getDestinationY() <= 0 ? getLocal().getPosition() : (new Tile(getDestinationX(), getDestinationY(), Client.getPlane())).toWorldTile();
    }

    @HookName("Player.CombatLevel")
    public int getCombatLevel() {
        return getPlayerInterface().getCombatLevel(this.instance);
    }

    @HookName("Player.Name")
    public String getName() {
        return StringUtils.format("" + getPlayerInterface().getName(this.instance));
    }

    @HookName("Player.Visible")
    public boolean isVisible() {
        return getPlayerInterface().isVisible(this.instance);
    }

    @HookName("Player.Composite")
    public PlayerComposite getComposite() {
        return getPlayerInterface().getComposite(this.instance);
    }

    public int getIndex() {
        int id = 0;
        Player[] players = getPlayers();
        if (players != null) {
            Player[] var3 = players;
            int var4 = players.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Player p = var3[var5];
                if (p != null && p.instance.equals(this.instance)) {
                    return id;
                }

                ++id;
            }
        }

        return -1;
    }

    public boolean interact(String action) {
        return Interact.interactHandler.playerInteract(action, this);
    }
}
