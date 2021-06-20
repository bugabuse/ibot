package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IWorld;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class World extends Wrapper {
    public static final int TYPE_REGULAR_FREE = 0;
    public static final int TYPE_REGULAR_MEMBERS = 1;

    public World(Object instance) {
        super(instance);
    }

    public static IWorld getWorldInterface() {
        return Bot.get().accessorInterface.worldInterface;
    }

    @HookName("World.id")
    public int getId() {
        return getWorldInterface().getId(this.instance);
    }

    @HookName("World.type")
    public int getType() {
        return getWorldInterface().getType(this.instance);
    }

    @HookName("World.playerCount")
    public int getPlayerCount() {
        return getWorldInterface().getPlayerCount(this.instance);
    }

    public int getRegularId() {
        return WorldHopping.toRegularWorldNumber(this.getId());
    }

    public boolean isSafeF2p() {
        return this.getType() % 2 == 0 && this.getType() != 4 && this.getType() != 128 && this.getId() != 401;
    }

    public boolean isSafeP2p() {
        return this.getType() == 1;
    }

    public String toString() {
        return "" + this.getRegularId();
    }
}
