package com.farm.ibot.api.methods.walking;

import com.farm.ibot.api.world.pathfinding.impl.WebPathFinder;
import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.api.wrapper.Tile;

import java.util.ArrayList;

public class WebWalkingCache {
    public static ArrayList<WebWalkingCache.PathCache> pathCache = new ArrayList();

    public static ArrayList<WebNode> getOrCreatePath(Tile start, Tile destination) {
        WebWalkingCache.PathCache temp = new WebWalkingCache.PathCache(start, destination, (ArrayList) null);
        WebWalkingCache.PathCache cache = (WebWalkingCache.PathCache) pathCache.stream().filter((p) -> {
            return p.contains(temp);
        }).findAny().orElse(null);
        if (cache == null) {

            ArrayList<WebNode> path = (new WebPathFinder()).findPath(start, destination);
            cache = new WebWalkingCache.PathCache(start, destination, path);
            if (cache.path.size() > 3) {
                pathCache.add(cache);
            }

            return cache.path;
        } else {
            return cache.path;
        }
    }

    static class PathCache {
        Tile start;
        Tile destination;
        ArrayList<WebNode> path;

        public PathCache(Tile start, Tile destination, ArrayList<WebNode> path) {
            this.start = start;
            this.destination = destination;
            this.path = path;
        }

        public boolean contains(WebWalkingCache.PathCache otherPath) {
            return otherPath.destination.equals(this.destination) && Walking.isInPath(20, otherPath.start, this.path);
        }
    }
}
