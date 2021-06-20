package com.farm.ibot.api.world.pathfinding.impl;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.api.world.pathfinding.PathFinder;
import com.farm.ibot.api.wrapper.Tile;

import java.util.*;

public class LocalPathFinder implements PathFinder {
    private int[][] flags;
    private int offX;
    private int offY;
    private boolean ignoreObject = false;
    private boolean onlyCheckReachable = false;

    public LocalPathFinder() {
    }

    public LocalPathFinder(boolean ignoreObject) {
        this.ignoreObject = ignoreObject;
    }

    public LocalPathFinder(boolean ignoreObject, boolean onlyCheckReachable) {
        this.ignoreObject = ignoreObject;
        this.onlyCheckReachable = onlyCheckReachable;
    }

    public static Tile getCollisionOffset(int plane) {
        return new Tile(0, 0, plane);
    }

    private static double dist(LocalPathFinder.Node start, LocalPathFinder.Node end) {
        return start.x != end.x && start.y != end.y ? 1.41421356D : 1.0D;
    }

    public Tile[] findPath(Tile start, Tile end) {
        return this.findPath(start, end, -1);
    }

    public Tile[] findPath(Tile start, Tile end, int maxDistance) {
        if (start != null && end != null && start.getZ() == end.getZ()) {
            ArrayList<LocalPathFinder.Node> objectTiles = new ArrayList();
            int destX;
            int destY;
            int currX;
            int currY;
            int baseX;
            int baseY;
            if (this.ignoreObject) {
                GameObject obj = GameObjects.getTopAt(end);
                destX = obj.getDefinition().width;
                destY = obj.getDefinition().height;

                for (currX = -((int) Math.ceil((double) destX / 2.0D)); currX <= (int) Math.ceil((double) destX / 2.0D); ++currX) {
                    for (currY = -((int) Math.ceil((double) destY / 2.0D)); currY <= (int) Math.ceil((double) destY / 2.0D); ++currY) {
                        baseX = obj.getPosition().getX() + currX;
                        baseY = obj.getPosition().getY() + currY;
                        Tile tile = (new Tile(baseX, baseY)).toLocalTile();
                        objectTiles.add(new LocalPathFinder.Node(tile.getX(), tile.getY(), end.getZ()));

                    }
                }
            }

            int currPlane = start.getZ();
            destX = end.toLocalTile().getX();
            destY = end.toLocalTile().getY();
            currX = start.toLocalTile().getX();
            currY = start.toLocalTile().getY();
            baseX = Client.getBaseX();
            baseY = Client.getBaseY();
            int plane = Client.getPlane();
            if (currPlane != plane) {
                return null;
            } else {
                Tile offset = getCollisionOffset(plane);
                this.flags = WorldData.getCollisionFlags(plane);
                this.offX = offset.getX();
                this.offY = offset.getY();
                if (this.flags != null && currX >= 0 && currY >= 0 && currX < this.flags.length && currY < this.flags.length) {
                    if (destX < 0 || destY < 0 || destX >= this.flags.length || destY >= this.flags.length) {
                        if (destX < 0) {
                            destX = 0;
                        } else if (destX >= this.flags.length) {
                            destX = this.flags.length - 1;
                        }

                        if (destY < 0) {
                            destY = 0;
                        } else if (destY >= this.flags.length) {
                            destY = this.flags.length - 1;
                        }
                    }

                    HashSet<LocalPathFinder.Node> open = new HashSet();
                    HashSet<LocalPathFinder.Node> closed = new HashSet();
                    LocalPathFinder.Node curr = new LocalPathFinder.Node(currX, currY, currPlane);
                    LocalPathFinder.Node dest = new LocalPathFinder.Node(destX, destY, currPlane);
                    curr.f = 1.0D;
                    open.add(curr);

                    while (!open.isEmpty()) {
                        curr = this.lowest_f(open);
                        if (curr.equals(dest)) {
                            return this.path(curr, baseX, baseY);
                        }

                        if (objectTiles.contains(curr)) {
                            return this.path(curr, baseX, baseY);
                        }

                        Iterator var18 = objectTiles.iterator();

                        LocalPathFinder.Node next;
                        while (var18.hasNext()) {
                            next = (LocalPathFinder.Node) var18.next();
                            if (next.distance(curr) <= 1) {
                                return this.path(curr, baseX, baseY);
                            }
                        }

                        if (maxDistance > -1 && this.path(curr, baseX, baseY).length > maxDistance) {
                            return null;
                        }

                        open.remove(curr);
                        closed.add(curr);
                        var18 = this.successors(curr).iterator();

                        while (var18.hasNext()) {
                            next = (LocalPathFinder.Node) var18.next();
                            if (!closed.contains(next)) {
                                double t = curr.g + dist(curr, next);
                                boolean use_t = false;
                                if (!open.contains(next)) {
                                    open.add(next);
                                    use_t = true;
                                } else if (t < next.g) {
                                    use_t = true;
                                }

                                if (use_t) {
                                    next.prev = curr;
                                    next.g = t;
                                    next.f = t;
                                }
                            }
                        }
                    }

                    if (objectTiles.contains(curr)) {
                        return this.path(curr, baseX, baseY);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    private LocalPathFinder.Node lowest_f(Set<LocalPathFinder.Node> open) {
        if (this.onlyCheckReachable) {
            return (LocalPathFinder.Node) open.iterator().next();
        } else {
            LocalPathFinder.Node best = null;
            Iterator var3 = open.iterator();

            while (true) {
                LocalPathFinder.Node t;
                do {
                    if (!var3.hasNext()) {
                        return best;
                    }

                    t = (LocalPathFinder.Node) var3.next();
                } while (best != null && t.f >= best.f);

                best = t;
            }
        }
    }

    private List<LocalPathFinder.Node> successors(LocalPathFinder.Node t) {
        LinkedList<LocalPathFinder.Node> tiles = new LinkedList();
        int x = t.x;
        int y = t.y;
        int z = t.z;
        int f_x = x - this.offX;
        int f_y = y - this.offY;
        int here = this.flags[f_x][f_y];
        int upper = this.flags.length - 1;
        if (f_y > 0 && (here & 32) == 0 && (this.flags[f_x][f_y - 1] & 19398912) == 0) {
            tiles.add(new LocalPathFinder.Node(x, y - 1, z));
        }

        if (f_x > 0 && (here & 128) == 0 && (this.flags[f_x - 1][f_y] & 19398912) == 0) {
            tiles.add(new LocalPathFinder.Node(x - 1, y, z));
        }

        if (f_y < upper && (here & 2) == 0 && (this.flags[f_x][f_y + 1] & 19398912) == 0) {
            tiles.add(new LocalPathFinder.Node(x, y + 1, z));
        }

        if (f_x < upper && (here & 8) == 0 && (this.flags[f_x + 1][f_y] & 19398912) == 0) {
            tiles.add(new LocalPathFinder.Node(x + 1, y, z));
        }

        if (f_x > 0 && f_y > 0 && (here & 224) == 0 && (this.flags[f_x - 1][f_y - 1] & 19398912) == 0 && (this.flags[f_x][f_y - 1] & 19399040) == 0 && (this.flags[f_x - 1][f_y] & 19398944) == 0) {
            tiles.add(new LocalPathFinder.Node(x - 1, y - 1, z));
        }

        if (f_x > 0 && f_y < upper && (here & 131) == 0 && (this.flags[f_x - 1][f_y + 1] & 19398912) == 0 && (this.flags[f_x][f_y + 1] & 19399040) == 0 && (this.flags[f_x - 1][f_y] & 19398914) == 0) {
            tiles.add(new LocalPathFinder.Node(x - 1, y + 1, z));
        }

        if (f_x < upper && f_y > 0 && (here & 56) == 0 && (this.flags[f_x + 1][f_y - 1] & 19398912) == 0 && (this.flags[f_x][f_y - 1] & 19398920) == 0 && (this.flags[f_x + 1][f_y] & 19398944) == 0) {
            tiles.add(new LocalPathFinder.Node(x + 1, y - 1, z));
        }

        if (f_x > 0 && f_y < upper && (here & 14) == 0 && (this.flags[f_x + 1][f_y + 1] & 19398912) == 0 && (this.flags[f_x][f_y + 1] & 19398920) == 0 && (this.flags[f_x + 1][f_y] & 19398914) == 0) {
            tiles.add(new LocalPathFinder.Node(x + 1, y + 1, z));
        }

        return tiles;
    }

    private Tile[] path(LocalPathFinder.Node end, int base_x, int base_y) {
        LinkedList<Tile> path = new LinkedList();

        for (LocalPathFinder.Node p = end; p != null; p = p.prev) {
            path.addFirst(p.get(base_x, base_y));
        }

        return (Tile[]) path.toArray(new Tile[path.size()]);
    }

    private final class Node {
        public final int x;
        public final int y;
        public final int z;
        public LocalPathFinder.Node prev;
        public double g;
        public double f;
        public boolean special;

        public Node(int x, int y, int z) {
            this(x, y, z, false);
        }

        public Node(int x, int y, int z, boolean special) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.special = special;
            this.g = this.f = 0.0D;
        }

        public int hashCode() {
            return this.x << 4 | this.y;
        }

        public boolean equals(Object o) {
            if (!(o instanceof LocalPathFinder.Node)) {
                return false;
            } else {
                LocalPathFinder.Node n = (LocalPathFinder.Node) o;
                return this.x == n.x && this.y == n.y && this.z == n.z;
            }
        }

        public String toString() {
            return "(" + this.x + "," + this.y + ")";
        }

        public Tile get(int baseX, int baseY) {
            return new Tile(this.x + baseX, this.y + baseY, this.z);
        }

        public int distance(LocalPathFinder.Node pos) {
            return (int) Math.sqrt(Math.pow((double) (pos.x - this.x), 2.0D) + Math.pow((double) (pos.y - this.y), 2.0D));
        }
    }
}
