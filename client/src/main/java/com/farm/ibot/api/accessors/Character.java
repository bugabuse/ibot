package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.ICharacter;
import com.farm.ibot.api.interfaces.Animable;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Character extends Renderable implements Animable {
    public Character(Object instance) {
        super(instance);
    }

    public static ICharacter getCharacterInterface() {
        return Bot.get().accessorInterface.characterReflectionInterface;
    }

    @HookName("Character.Frame1")
    public int getFrame1() {
        return getCharacterInterface().getFrame1(this.instance);
    }

    @HookName("Character.HealthBars")
    public Iterable getHealthBarIterable() {
        return getCharacterInterface().getHealthBarIterable(this.instance);
    }

    @HookName("Character.Frame2")
    public int getFrame2() {
        return getCharacterInterface().getFrame2(this.instance);
    }

    @HookName("Character.InteractingIndex")
    public int getInteractingIndex() {
        return getCharacterInterface().getInteractingIndex(this.instance);
    }

    @HookName("Character.WalkingQueueSize")
    public int getWalkingQueueSize() {
        return getCharacterInterface().getWalkingQueueSize(this.instance);
    }

    @HookName("Character.WalkingQueueX")
    public int[] getWalkingQueueX() {
        return getCharacterInterface().getWalkingQueueX(this.instance);
    }

    @HookName("Character.WalkingQueueY")
    public int[] getWalkingQueueY() {
        return getCharacterInterface().getWalkingQueueY(this.instance);
    }

    @HookName("Character.HitCycles")
    public int[] getHitCycles() {
        return getCharacterInterface().getHitCycles(this.instance);
    }

    @HookName("Character.AnimableX")
    public int getAnimableX() {
        return getCharacterInterface().getAnimableX(this.instance);
    }

    @HookName("Character.AnimableY")
    public int getAnimableY() {
        return getCharacterInterface().getAnimableY(this.instance);
    }

    @HookName("Renderable.ModelHeight")
    public int getModelHeight() {
        return getCharacterInterface().getModelHeight(this.instance);
    }

    @HookName("Character.Animation")
    public int getAnimation() {
        return getCharacterInterface().getAnimation(this.instance);
    }

    @HookName("Character.Orientation")
    public int getOrientation() {
        return getCharacterInterface().getOrientation(this.instance);
    }

    @HookName("Character.npcCycle")
    public int getNpcCycle() {
        return getCharacterInterface().getNpcCycle(this.instance);
    }

    public HealthBar getHealthBar() {
        Debug.log(this.getHealthBarIterable());
        Debug.log(this.getHealthBarIterable().getNode());
        Debug.log(this.getHealthBarIterable().getNode().getNext());
        return new HealthBar(this.getHealthBarIterable().getNode().getNext().instance);
    }

    public boolean isReachable() {
        return this.getPosition().isReachable();
    }

    public boolean isDead() {
        return this.getHealthBar().getData().getCurrentHealth() <= 0;
    }

    public boolean isInCombat() {
        int tick = Client.getGameTick() - 130;
        int[] hitTicks = this.getHitCycles();
        int[] var3 = hitTicks;
        int var4 = hitTicks.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            int loopCycle = var3[var5];
            if (loopCycle > tick) {
                return true;
            }
        }

        return false;
    }

    public boolean isInteractingWithMe() {
        return Player.getLocal().equals(this.getInteracting());
    }

    public Character getInteracting() {
        int index = this.getInteractingIndex();
        if (index == -1) {
            return null;
        } else {
            if (index < 32768) {
                Npc[] npcs = Npc.getLocalNpcs();
                if (npcs != null && npcs.length > index) {
                    return npcs[index];
                }
            } else {
                index -= 32768;
                if (index == Client.getPlayerIndex()) {
                    return Player.getLocal();
                }

                Player[] players = Player.getPlayers();
                if (players != null && players.length > index) {
                    return players[index];
                }
            }

            return null;
        }
    }

    public boolean isMoving() {
        return this.getWalkingQueueSize() > 0;
    }
}
