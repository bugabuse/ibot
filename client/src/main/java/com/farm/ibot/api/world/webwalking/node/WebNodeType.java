package com.farm.ibot.api.world.webwalking.node;

import java.lang.reflect.Type;

public enum WebNodeType {
    REGULAR(WebNode.class),
    OBJECT(ObjectWebNode.class),
    STAIRS(StairsWebNode.class),
    TELEPORT(TeleportWebNode.class),
    NPC(NpcWebNode.class);

    public Type typeClass;

    private WebNodeType(Type typeClass) {
        this.typeClass = typeClass;
    }

    public Type getTypeClass() {
        return this.typeClass;
    }
}
