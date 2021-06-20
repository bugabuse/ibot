package com.farm.ibot.api.methods.walking;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.pathfinding.impl.LocalPathFinder;
import com.farm.ibot.api.world.pathfinding.impl.StraightPathFinder;
import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.api.wrapper.Tile;

import java.util.ArrayList;

public class Walking {
    public static boolean walkNext(Tile[] path) {
        return walkNext(path, 5, false);
    }

    public static boolean walkNext(Tile[] path, int areaRadius) {
        return walkNext(path, areaRadius, false);
    }

    public static boolean walkTo(Tile tile) {
        return walkTo(tile, 0);
    }

    public static boolean walkTo(Tile tile, int areaRadius) {
        if (areaRadius >= 2) {
            Interact.interactHandler.walk(randomizeTile(tile));
        } else {
            Interact.interactHandler.walk(tile);
        }

        Time.sleep(1200);
        return tile.distance() <= areaRadius;
    }

    public static boolean walkNext(Tile[] path, int areaRadius, boolean reversed) {
        if (path != null && path.length >= 1) {
            if (Player.getDestination().distance() > 8) {
                return false;
            } else {
                if (reversed) {
                    path = reversePath(path);
                }

                Tile bestTile = WebWalking.getBestTile(path);
                Tile finalDestination = path[path.length - 1];
                if (bestTile != null && bestTile.distance() <= 30) {
                    if (bestTile instanceof WebNode) {
                        if (!((WebNode) finalDestination).hasTraversed((WebNode) null, areaRadius)) {
                            int index = getTileIndex(bestTile, path);
                            WebNode webNode = (WebNode) bestTile;
                            WebNode nextNode = path.length > index + 1 ? (WebNode) path[index + 1] : null;
                            webNode.traverse(nextNode, areaRadius > 0);
                            return ((WebNode) finalDestination).hasTraversed((WebNode) null, areaRadius);
                        } else {
                            return ((WebNode) finalDestination).hasTraversed((WebNode) null, areaRadius);
                        }
                    } else {
                        if (bestTile != null && bestTile.distance() > 0) {
                            walkTo(randomizeTile(bestTile));
                        }

                        return finalDestination.isReachable() && finalDestination.distance(Player.getLocal().getPosition()) <= areaRadius;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private static Tile[] reversePath(Tile[] path) {
        Tile[] temp = new Tile[path.length];

        for (int i = 0; i < path.length; ++i) {
            temp[i] = path[path.length - 1 - i];
        }

        return temp;
    }

    public static boolean isRunEnabled() {
        return Config.get(173) == 1;
    }

    public static void setRun(boolean run) {
        if (isRunEnabled() != run && (!run || Client.getRunEnergy() >= 5)) {
            Widgets.get(160, 22).interact("");
            Time.sleep(800, () -> {
                return isRunEnabled() == run;
            });
        }
    }

    public static int getDestinationDistance() {
        return Player.getLocal().getPosition().distance(Player.getDestination());
    }

    public static Tile randomizeTile(Tile tile) {
        ArrayList<Tile> available = new ArrayList();
        available.add(tile);
        if (tile != null) {
            for (int x = -3; x <= 3; ++x) {
                for (int y = -3; y <= 3; ++y) {
                    Tile t = tile.add(x, y);
                    Tile[] path = (new LocalPathFinder()).findPath(tile, t, 6);
                    if (path != null && path.length < 5) {
                        available.add(t);
                    }
                }
            }
        }

        Tile t = (Tile) available.get(Random.next(0, available.size()));
        return t;
    }

    private static int getTileIndex(Tile tileToCheck, Tile[] path) {
        int i = 0;
        Tile[] var3 = path;
        int var4 = path.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Tile tile = var3[var5];
            if (tile.equals(tileToCheck)) {
                break;
            }

            ++i;
        }

        return i;
    }

    public static int getClosestTileIndex(Tile tile, Tile[] path) {
        int bestDistance = Integer.MAX_VALUE;
        int index = 0;

        for (int i = 0; path != null && i < path.length; ++i) {
            Tile t = path[i];
            if (tile.distance(t) < bestDistance && t.isReachable()) {
                bestDistance = tile.distance(t);
                index = i;
            }
        }

        return index;
    }

    private static Tile getClosestTile(Tile tile, Tile[] path) {
        return path != null && path.length > 0 ? path[getClosestTileIndex(tile, path)] : null;
    }

    public static int distanceToPath(Tile tile, Tile[] path) {
        return Tile.distance(tile, getClosestTile(tile, makePathStriaight(path)));
    }

    public static boolean isInPath(Tile destination, ArrayList<WebNode> path) {
        return isInPath(destination, (Tile[]) path.toArray(new Tile[path.size()]));
    }

    public static boolean isInPath(Tile destination, Tile[] path) {
        return isInPath(destination, path, 4);
    }

    public static boolean isInPath(Tile destination, Tile[] path, int distance) {
        return distanceToPath(destination, path) <= distance;
    }

    public static boolean isInPath(int maxDistance, Tile destination, ArrayList<WebNode> path) {
        return Tile.distance(destination, getClosestTile(destination, (Tile[]) path.toArray(new Tile[path.size()]))) <= maxDistance;
    }

    public static Tile[] makePathStriaight(Tile[] path) {
        ArrayList<Tile> pathList = new ArrayList();

        for (int i = 0; i < path.length - 1; ++i) {
            Tile[] var3 = (new StraightPathFinder()).findPath(path[i], path[i + 1]);
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Tile newTile = var3[var5];
                pathList.add(newTile);
            }
        }

        return (Tile[]) pathList.toArray(new Tile[pathList.size()]);
    }
}
