package com.farm.ibot.api.world.webwalking.node;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;

public class StairsWebNode extends ObjectWebNode {
    public String climbUpAction;
    public String climbDownAction;

    public StairsWebNode() {
        this.type = WebNodeType.STAIRS;
    }

    public StairsWebNode(Tile position, String objectName, String climbUpAction, String climbDownAction) {
        super(position);
        this.objectName = objectName;
        this.climbUpAction = climbUpAction;
        this.climbDownAction = climbDownAction;
        this.type = WebNodeType.STAIRS;
    }

    public StairsWebNode(Tile position, WebNode parent, String objectName, String climbUpAction, String climbDownAction) {
        super(position, parent);
        this.objectName = objectName;
        this.climbUpAction = climbUpAction;
        this.climbDownAction = climbDownAction;
        this.type = WebNodeType.STAIRS;
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
        GameObject doors = GameObjects.get(this.objectName, this);
        String action = nextNode.getZ() > this.getZ() ? this.climbUpAction : this.climbDownAction;
        if (nextNode.getZ() == this.getZ()) {
            return true;
        } else if (doors != null && doors.getPosition().distance(this) < 4) {
            doors.interact(action);
            if (nextNode != null) {
                nextNode.getClass();
                return Time.sleep(nextNode::isReachable);
            } else if (Time.waitObjectDissapear(doors)) {
                Time.sleep(300, 600);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
