package com.farm.ibot.api.methods.walking;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.methods.Combat;
import com.farm.ibot.api.world.pathfinding.impl.ExtendedStraightPathFinder;
import com.farm.ibot.api.world.pathfinding.impl.StraightPathFinder;
import com.farm.ibot.api.world.pathfinding.impl.WebPathFinder;
import com.farm.ibot.api.world.webwalking.node.NpcWebNode;
import com.farm.ibot.api.world.webwalking.node.ObjectWebNode;
import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.api.wrapper.Tile;

import java.util.ArrayList;

public class WebWalking {
    public static Condition RUNNING_MANUAL = null;
    public static Condition RUNNING_ALWAYS_OFF = () -> {
        return false;
    };
    public static Condition RUNNING_ALWAYS_ON = () -> {
        return true;
    };
    public static Condition RUNNING_ON_COMBAT = Combat::isUnderAttack;
    public static Condition RUNNING_ON_COMBAT_OR_ENOUGH_ENERGY = () -> {
        return Client.getRunEnergy() >= 25 || Combat.isUnderAttack();
    };
    public static Condition enableRunningCondition;

    static {
        enableRunningCondition = RUNNING_ON_COMBAT_OR_ENOUGH_ENERGY;
    }

    public static boolean walkTo(Tile tile, Tile... includeTiles) {
        return walkTo(tile, 5, includeTiles);
    }

    public static boolean walkTo(Tile tile, int areaRadius, Tile... includeTiles) {
        try {
            return walkToEnsurePath(tile, areaRadius, includeTiles);
        } catch (PathNotFoundException var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public static boolean walkToEnsurePath(Tile tile, int areaRadius, Tile... includeTiles) throws PathNotFoundException {
        if ((new WebNode(tile)).hasTraversed((WebNode) null, areaRadius)) {
            return true;
        } else if ((new WebNode(tile)).isReachable()) {
            return Walking.walkTo(tile, areaRadius);
        } else {
            if (enableRunningCondition != null && enableRunningCondition != null && enableRunningCondition.active()) {
                Walking.setRun(true);
            }

            ArrayList<WebNode> path = (new WebPathFinder()).includeTiles(includeTiles).findPath(Player.getLocal().getPosition(), tile);
            if (path != null) {
                path.add((new WebNode(tile)).finalInPath(true));
                return Walking.walkNext((Tile[]) path.toArray(new Tile[path.size()]), areaRadius);
            } else {
                throw new PathNotFoundException();
            }
        }
    }

    public static boolean canFindPath(Tile tile) {
        return tile.distance() < 20 || (new WebPathFinder()).findPath(Player.getLocal().getPosition(), tile).size() > 0;
    }

    public static Tile getBestTile(Tile[] path) {
        int closestIndex = Walking.getClosestTileIndex(Player.getLocal().getPosition(), path);
        Tile best = path[closestIndex];

        for (int i = closestIndex; i < path.length; ++i) {
            Tile tile = path[i];
            if (tile.distance() >= 30) {
                return best;
            }

            if (tile instanceof WebNode) {
                WebNode currentNode = (WebNode) tile;
                WebNode nextNode = path.length > i + 1 ? (WebNode) path[i + 1] : null;
                if ((tile instanceof ObjectWebNode || tile instanceof NpcWebNode) && !currentNode.hasTraversed(nextNode, 4)) {
                    if (tile.distance() <= 16 && tile.isReachable()) {
                        best = tile;
                    }
                } else {
                    if (nextNode == null) {
                        return best;
                    }

                    if (nextNode.distance(currentNode) <= 30) {
                        Tile[] tempPath = (new ExtendedStraightPathFinder()).findPath(currentNode, nextNode);
                        if (tempPath.length <= 1 || tempPath == null) {
                            tempPath = (new StraightPathFinder()).findPath(currentNode, nextNode);
                        }

                        Tile[] var8 = tempPath;
                        int var9 = tempPath.length;

                        for (int var10 = 0; var10 < var9; ++var10) {
                            Tile t = var8[var10];
                            if (t.distance() <= 16 && t.isReachable()) {
                                best = t;
                            }
                        }
                    }
                }
            }
        }

        return best;
    }
}
