package com.farm.ibot.api.world.webwalking.node;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.world.webwalking.requirement.Requirement;
import com.farm.ibot.api.wrapper.Tile;

import java.util.ArrayList;

public class WebNode extends Tile {
    public transient boolean finalInPath;
    public ArrayList<Requirement> requirements;
    public WebNodeType type;
    private transient ArrayList<WebNode> destinations;
    private ArrayList<Integer> destinationNodeIds;
    private int id;

    public WebNode() {
        this.destinations = new ArrayList();
        this.finalInPath = false;
        this.requirements = new ArrayList();
        this.type = WebNodeType.REGULAR;
        this.destinationNodeIds = new ArrayList();
        this.id = -1;
        this.type = WebNodeType.REGULAR;
    }

    public WebNode(Tile position) {
        super(position.getX(), position.getY(), position.getZ());
        this.destinations = new ArrayList();
        this.finalInPath = false;
        this.requirements = new ArrayList();
        this.type = WebNodeType.REGULAR;
        this.destinationNodeIds = new ArrayList();
        this.id = -1;
        this.id = Random.next(0, 2147483646);
        this.type = WebNodeType.REGULAR;
    }

    public WebNode(Tile position, WebNode parent) {
        this(position);
        parent.addDestination(this);
        this.addDestination(parent);
    }

    public void setDestinationNodes(WebNode[] parent) {
        this.destinations = new ArrayList();
        WebNode[] var2 = parent;
        int var3 = parent.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            WebNode node = var2[var4];
            if (this.destinationNodeIds.contains(node.id)) {
                this.destinations.add(node);
            }
        }

    }

    public void removeDestinations(WebNode... nodes) {
        WebNode[] var2 = nodes;
        int var3 = nodes.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            WebNode node = var2[var4];
            this.removeDestination(node);
        }

    }

    public void removeDestination(WebNode node) {
        if (node != null) {
            this.destinationNodeIds.remove(node.getId());
            this.destinations.remove(node);
        }

    }

    public ArrayList<WebNode> getDestinations() {
        return this.destinations;
    }

    public void connectOneWay(WebNode node) {
        this.addDestination(node);
    }

    public void connectBoth(WebNode node) {
        node.addDestination(this);
        this.addDestination(node);
    }

    public void addDestination(WebNode node) {
        if (!this.destinations.contains(node)) {
            this.destinations.add(node);
            this.destinationNodeIds.add(node.id);
        }

    }

    public boolean traverse(WebNode nextNode, boolean randomize) {
        if (nextNode != null && !nextNode.finalInPath) {
            if (Player.getDestination().distance() <= 6 || Player.getDestination().distance(this) >= 3) {
                Walking.walkTo(Walking.randomizeTile(this));
            }
        } else if (randomize) {
            Walking.walkTo(Walking.randomizeTile(this));
        } else {
            Walking.walkTo(this);
        }

        return true;
    }

    public boolean hasTraversed(WebNode nextNode, int areaRadius) {
        if (nextNode != null) {
            return Walking.distanceToPath(Player.getLocal().getPosition(), new Tile[]{this, nextNode}) <= areaRadius;
        } else {
            return this.distance() <= areaRadius && this.isReachable();
        }
    }

    public boolean hasPassedRequirements() {
        return this.requirements == null || this.requirements.stream().allMatch(Requirement::hasPassed);
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.getClass().getSimpleName() + " [" + this.getX() + ", " + this.getY() + " ," + this.getZ() + "] " + this.id;
    }

    public boolean equals(Object other) {
        return other != null && ((WebNode) other).id == this.id;
    }

    public WebNode finalInPath(boolean isFinal) {
        this.finalInPath = isFinal;
        return this;
    }
}
