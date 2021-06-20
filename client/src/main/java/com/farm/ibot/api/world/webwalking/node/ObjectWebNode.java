package com.farm.ibot.api.world.webwalking.node;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;

public class ObjectWebNode extends WebNode {
    public int objectId;
    public String objectName;
    public String action;

    public ObjectWebNode() {
        this.objectId = -1;
        this.type = WebNodeType.OBJECT;
    }

    public ObjectWebNode(Tile position) {
        super(position);
        this.objectId = -1;
        this.type = WebNodeType.OBJECT;
    }

    public ObjectWebNode(Tile position, WebNode parent) {
        super(position, parent);
        this.objectId = -1;
        this.type = WebNodeType.OBJECT;
    }

    public ObjectWebNode(Tile position, String objectName, String action) {
        this(position);
        this.objectName = objectName;
        this.action = action;
    }

    public ObjectWebNode(Tile position, WebNode parent, String objectName, String action) {
        this(position, parent);
        this.objectName = objectName;
        this.action = action;
    }

    public boolean traverse(WebNode nextNode, boolean randomize) {
        if (nextNode != null && !nextNode.isReachable()) {
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
        GameObject doors = this.objectId == -1 ? GameObjects.get(this.objectName, this) : GameObjects.get(this.objectId, this);
        if (doors != null && doors.getPosition().distance(this) < 4) {
            doors.interact(this.action);
            Time.sleep(1200, 2400);
            return this.hasTraversed(nextNode, 1);
        } else {
            return this.hasTraversed(nextNode, 1);
        }
    }
}
