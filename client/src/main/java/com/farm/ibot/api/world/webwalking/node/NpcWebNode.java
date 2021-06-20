package com.farm.ibot.api.world.webwalking.node;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;

public class NpcWebNode extends WebNode {
    public String npcName;
    public String action;
    public String[] dialogues;

    public NpcWebNode() {
        this.type = WebNodeType.NPC;
    }

    public NpcWebNode(Tile position, String npcName, String action, String[] dialogues) {
        super(position);
        this.npcName = npcName;
        this.action = action;
        this.dialogues = dialogues;
        this.type = WebNodeType.NPC;
    }

    public NpcWebNode(Tile position, WebNode parent, String npcName, String action, String[] dialogues) {
        super(position, parent);
        this.npcName = npcName;
        this.action = action;
        this.dialogues = dialogues;
        this.type = WebNodeType.NPC;
    }

    public boolean traverse(WebNode nextNode, boolean randomize) {
        if (InputBox.isOpen()) {
            InputBox.input("0");
            return false;
        } else if (Dialogue.isInDialouge()) {
            Dialogue.goNext(this.dialogues);
            return false;
        } else if (nextNode != null && !nextNode.isReachable()) {
            return this.handleObject(nextNode);
        } else {
            Walking.walkTo(this);
            return true;
        }
    }

    public boolean hasTraversed(WebNode nextNode, int areaRadius) {
        return nextNode == null && this.distance() <= areaRadius || nextNode != null && nextNode.isReachable();
    }

    private boolean handleObject(WebNode nextNode) {
        Npc npc = Npcs.get(this.npcName);
        if (npc != null && npc.isReachable()) {
            npc.interact(this.action);
            return nextNode != null ? Time.sleep(() -> {
                return nextNode.isReachable() || Dialogue.isInDialouge();
            }) : Time.sleep(() -> {
                return !npc.exists() || Dialogue.isInDialouge();
            });
        } else {
            return true;
        }
    }
}
