// Decompiled with: Procyon 0.5.36
package com.farm.ibot.api.util;

import com.farm.ibot.core.Bot;

import java.util.HashMap;

public class WrapperCache<T> {
    private HashMap<Bot, WrapperCacheNode> cache;

    public WrapperCache() {
        this.cache = new HashMap<Bot, WrapperCacheNode>();
    }

    public T getOrCache(final CacheWrapperRunnable cacheRunnable) {
        WrapperCacheNode cacheNode = this.cache.get(Bot.get());
        if (cacheNode == null) {
            cacheNode = new WrapperCacheNode(null, 0L);
            this.cache.put(Bot.get(), cacheNode);
        }
        if (System.currentTimeMillis() - cacheNode.lastUpdate > 100L || cacheNode.object == null) {
            cacheNode.object = (T) cacheRunnable.run();
            cacheNode.lastUpdate = System.currentTimeMillis();
        }
        return cacheNode.object;
    }

    public interface CacheWrapperRunnable<T> {
        T run();
    }

    class WrapperCacheNode {
        public long lastUpdate;
        public T object;

        public WrapperCacheNode(final T object, final long lastUpdate) {
            this.lastUpdate = lastUpdate;
            this.object = object;
        }
    }
}
