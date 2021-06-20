package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.util.Random;

public class ItemOnCharacterAction extends Action {
    public ItemOnCharacterAction(Npc character) {
        super(0, 0, 7, character.getIndex(), "Use", "Use", Random.next(0, 300), Random.next(0, 300));
    }

    public static ItemOnCharacterAction create(Character character) {
        return character instanceof Npc ? new ItemOnCharacterAction((Npc) character) : null;
    }
}
