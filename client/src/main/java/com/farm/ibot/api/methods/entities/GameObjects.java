// Decompiled with: CFR 0.150
package com.farm.ibot.api.methods.entities;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Region;
import com.farm.ibot.api.accessors.RegionTile;
import com.farm.ibot.api.accessors.gameobject.BoundaryObject;
import com.farm.ibot.api.accessors.gameobject.FloorObject;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.accessors.gameobject.WallObject;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.util.Sorting;
import com.farm.ibot.api.wrapper.Jarvis;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GameObjects {
    public static HashMap<Bot, GameObjectsCache> gameObjectsCache = new HashMap();
    public static EntitySearcher<GameObject> normal = new EntitySearcher<GameObject>() {

        @Override
        public GameObject get(int id, Tile nearestTo) {
            return Sorting.getNearest(nearestTo, GameObjects.getAll(f -> f.getId() == id));
        }

        @Override
        public GameObject get(int id) {
            return this.get(id, Player.getLocal().getPosition());
        }

        @Override
        public GameObject get(String name, Tile nearestTo) {
            return Sorting.getNearest(nearestTo, GameObjects.getAll(f -> f.getName().equalsIgnoreCase(name)));
        }

        @Override
        public GameObject get(String name) {
            return this.get(name, Player.getLocal().getPosition());
        }

        @Override
        public GameObject get(Filter<GameObject> filter) {
            return Sorting.getNearest(GameObjects.getAll(filter));
        }
    };
    public static EntitySearcher<GameObject> best = new EntitySearcher<GameObject>() {

        @Override
        public GameObject get(int id, Tile nearestTo) {
            return Sorting.getBest(nearestTo, GameObjects.getAll(f -> f.getId() == id));
        }

        @Override
        public GameObject get(int id) {
            return this.get(id, Player.getLocal().getPosition());
        }

        @Override
        public GameObject get(String name, Tile nearestTo) {
            return Sorting.getBest(nearestTo, GameObjects.getAll(f -> f.getName().equalsIgnoreCase(name)));
        }

        @Override
        public GameObject get(String name) {
            return this.get(name, Player.getLocal().getPosition());
        }

        @Override
        public GameObject get(Filter<GameObject> filter) {
            return Sorting.getBest(GameObjects.getAll(filter));
        }
    };

    public static GameObject get(int id, Tile nearestTo) {
        return normal.get(id, nearestTo);
    }

    public static GameObject get(String name, Tile nearestTo) {
        return normal.get(name, nearestTo);
    }

    public static GameObject get(int id) {
        return normal.get(id, Player.getLocal().getPosition());
    }

    public static GameObject get(String name) {
        return normal.get(name, Player.getLocal().getPosition());
    }

    public static GameObject get(Filter<GameObject> filter) {
        return normal.get(filter);
    }

    public static ArrayList<GameObject> getAll(String name) {
        return GameObjects.getAll((GameObject f) -> f.getName().equalsIgnoreCase(name));
    }

    public static HashMap<Long, GameObject> getAllHashmap() {
        Bot bot = Bot.get();
        GameObjectsCache cache = gameObjectsCache.get(bot);
        if (cache == null) {
            cache = new GameObjectsCache(null, 0L);
            gameObjectsCache.put(bot, cache);
        }
        if (System.currentTimeMillis() - cache.lastTime > 300L || cache.gameObjects == null) {
            cache.gameObjects = GameObjects.fetchAllObjects();
            cache.lastTime = System.currentTimeMillis();
        }
        return cache.gameObjects;
    }

    public static ArrayList<GameObject> getAll() {
        return new ArrayList<GameObject>(GameObjects.getAllHashmap().values());
    }

    public static ArrayList<GameObject> getAll(Filter<GameObject> filter) {
        ArrayList<GameObject> objects = GameObjects.getAll();
        return objects.stream().filter(filter::accept).collect(Collectors.toCollection(ArrayList::new));
    }

    private static HashMap<Long, GameObject> fetchAllObjects() {
        HashMap<Long, GameObject> objects = new HashMap<Long, GameObject>();
        RegionTile[][][] tiles = Region.getRegion().getTiles();
        int z = Client.getPlane();
        for (int x = 0; x < 104; ++x) {
            for (int y = 0; y < 104; ++y) {
                FloorObject floor;
                BoundaryObject boundary;
                WallObject wall;
                if (tiles[z][x][y] == null) continue;
                GameObject[] tileObjects = tiles[z][x][y].getGameObjects();
                if (tileObjects != null) {
                    for (GameObject object : tileObjects) {
                        if (object == null || objects.containsKey(object.getUid())) continue;
                        objects.put(object.getUid(), object);
                    }
                }
                if ((wall = tiles[z][x][y].getWallObject()) != null && !objects.containsKey(((GameObject) wall).getUid())) {
                    objects.put(((GameObject) wall).getUid(), wall);
                }
                if ((boundary = tiles[z][x][y].getBoundaryObject()) != null && !objects.containsKey(((GameObject) boundary).getUid())) {
                    objects.put(((GameObject) boundary).getUid(), boundary);
                }
                if ((floor = tiles[z][x][y].getFloorObject()) == null || objects.containsKey(((GameObject) floor).getUid()))
                    continue;
                objects.put(((GameObject) floor).getUid(), floor);
            }
        }
        return objects;
    }

    public static GameObject getAny(Filter<GameObject> filter) {
        return GameObjects.getAll().stream().filter(filter::accept).findAny().orElse(null);
    }

    public static GameObject getTopAt(Tile tile) {
        ArrayList<GameObject> objects = GameObjects.getAt(tile);
        return objects.size() > 0 ? objects.get(objects.size() - 1) : null;
    }

    public static ArrayList<GameObject> getAt(Tile tile) {
        FloorObject floor;
        BoundaryObject boundary;
        WallObject wall;
        ArrayList<GameObject> objects = new ArrayList<GameObject>();
        RegionTile regionTile = RegionTile.get((tile = tile.toLocalTile()).getX(), tile.getY(), Client.getPlane());
        if (regionTile == null) {
            return objects;
        }
        GameObject[] tileObjects = regionTile.getGameObjects();
        if (tileObjects != null) {
            for (GameObject object : tileObjects) {
                if (object == null) continue;
                objects.add(object);
            }
        }
        if ((wall = regionTile.getWallObject()) != null) {
            objects.add(wall);
        }
        if ((boundary = regionTile.getBoundaryObject()) != null) {
            objects.add(boundary);
        }
        if ((floor = regionTile.getFloorObject()) != null) {
            objects.add(floor);
        }
        return objects;
    }

    public static Polygon combinedHull(Filter<GameObject> filter) {
        ArrayList<GameObject> gameObjects = GameObjects.getAll(filter);
        ArrayList<Point> convexHull = new ArrayList<Point>();
        for (GameObject gameObject : gameObjects) {
            convexHull.addAll(gameObject.getConvexHullPoints());
        }
        List<Point> points = Jarvis.convexHull(convexHull);
        Polygon p = new Polygon();
        for (Point point : points) {
            p.addPoint((int) point.getX(), (int) point.getY());
        }
        return p;
    }

    private static class GameObjectsCache {
        public HashMap<Long, GameObject> gameObjects;
        public long lastTime;

        public GameObjectsCache(HashMap<Long, GameObject> gameObjects, long lastTime) {
            this.gameObjects = gameObjects;
            this.lastTime = lastTime;
        }
    }
}
