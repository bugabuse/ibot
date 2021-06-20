package com.farm.ibot.api.world.pathfinding.impl;

import com.farm.ibot.api.methods.TileReach;
import com.farm.ibot.api.world.webwalking.WebData;
import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.api.wrapper.Tile;

import java.util.*;

public class WebPathFinder {
    private boolean checkReachable = true;
    private List<Tile> includedTiles = new ArrayList();

    public WebPathFinder() {
    }

    public WebPathFinder(boolean checkReachable) {
        this.checkReachable = checkReachable;
    }

    public static WebNode getClosestReachableNode(Tile tile) {
        WebNode closest = null;
        int lastDistance = Integer.MAX_VALUE;
        Iterator var3 = WebData.getNodes().iterator();

        while (var3.hasNext()) {
            WebNode node = (WebNode) var3.next();
            if (node.getPosition().distance(tile) < 30 && node.getPosition().distance(tile) < lastDistance && node.isReachable()) {
                closest = node;
                lastDistance = node.getPosition().distance(tile);
            }
        }

        return closest;
    }

    public static WebNode getClosestReachableEndNode(Tile tile) {
        WebNode closest = null;
        int lastDistance = Integer.MAX_VALUE;
        Iterator var3 = WebData.getNodes().iterator();

        while (var3.hasNext()) {
            WebNode node = (WebNode) var3.next();
            if (node.getPosition().distance(tile) < 30 && node.getPosition().distance(tile) < lastDistance && TileReach.isReachable(node, tile)) {
                closest = node;
                lastDistance = node.getPosition().distance(tile);
            }
        }

        return closest;
    }

    public static WebNode getClosestNode(Tile tile) {
        WebNode closest = null;
        int lastDistance = Integer.MAX_VALUE;
        Iterator var3 = WebData.getNodes().iterator();

        while (var3.hasNext()) {
            WebNode node = (WebNode) var3.next();
            if (node.getPosition().distance(tile) < lastDistance) {
                closest = node;
                lastDistance = node.getPosition().distance(tile);
            }
        }

        return closest;
    }

    public WebPathFinder includeTiles(Tile... tiles) {
        this.includedTiles = Arrays.asList(tiles);
        return this;
    }

    public ArrayList<WebNode> findPath(Tile startTile, Tile endTile) {
        if (this.includedTiles.size() <= 0) {
            return this.findRegularPath(startTile, endTile);
        } else {
            ArrayList<WebNode> path = new ArrayList();
            Tile currentStart = startTile;

            Tile includedTile;
            for (Iterator var5 = this.includedTiles.iterator(); var5.hasNext(); currentStart = includedTile) {
                includedTile = (Tile) var5.next();
                path.addAll(this.findRegularPath(currentStart, includedTile));
            }

            path.addAll(this.findRegularPath(currentStart, endTile));
            return path;
        }
    }

    private ArrayList<WebNode> findRegularPath(Tile startTile, Tile endTile) {
        HashMap<WebNode, WebNode> parentNodes = new HashMap();
        WebNode startNode = this.checkReachable ? getClosestReachableNode(startTile) : getClosestNode(startTile);
        WebNode goalNode;
        if (endTile.distance() < 15) {
            goalNode = this.checkReachable ? getClosestReachableEndNode(endTile) : getClosestNode(endTile);
        } else {
            goalNode = getClosestNode(endTile);
        }

        LinkedList closedList = new LinkedList();
        LinkedList openList = new LinkedList();
        openList.add(startNode);
        parentNodes.put(startNode, null);
        if (startNode != null && goalNode != null) {
            while (true) {
                WebNode node;
                do {
                    if (openList.isEmpty()) {
                        return new ArrayList();
                    }

                    node = (WebNode) openList.getFirst();
                    openList.removeFirst();
                    if (node == goalNode) {
                        return this.constructPath(startNode, goalNode, parentNodes);
                    }
                } while (node == null);

                closedList.add(node);
                Iterator var9 = node.getDestinations().iterator();

                while (var9.hasNext()) {
                    WebNode neighborNode = (WebNode) var9.next();
                    if (neighborNode.hasPassedRequirements() && !closedList.contains(neighborNode) && !openList.contains(neighborNode)) {
                        parentNodes.put(neighborNode, node);
                        openList.add(neighborNode);
                    }
                }
            }
        } else {
            return new ArrayList();
        }
    }

    private ArrayList<WebNode> constructPath(WebNode start, WebNode node, HashMap<WebNode, WebNode> parentNodes) {
        ArrayList path;
        for (path = new ArrayList(); parentNodes.get(node) != null; node = (WebNode) parentNodes.get(node)) {
            path.add(0, node);
        }

        path.add(0, start);
        return path;
    }
}
