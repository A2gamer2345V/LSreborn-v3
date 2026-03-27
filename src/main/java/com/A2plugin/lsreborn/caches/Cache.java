package com.A2plugin.lsreborn.caches;

import com.A2plugin.lsreborn.LSReborn;

import java.util.HashSet;
import java.util.Set;

public abstract class Cache<T> {
    private final Set<T> cachedData;
    private final LSReborn plugin;

    public Cache(LSReborn plugin) {
        this.plugin = plugin;
        this.cachedData = new HashSet<>();
        reloadCache();
    }

    /**
     * Reload the cache from the database
     */
    public abstract void reloadCache();

    /**
     * Get a set of all cached data
     */
    public Set<T> getCachedData() {
        return new HashSet<>(cachedData);
    }

    /**
     * Add an item to the cache
     * @param item The item to add
     */
    public void addItem(T item) {
        cachedData.add(item);
    }

    /**
     * Remove an item from the cache
     * @param item The item to remove
     */
    public void removeItem(T item) {
        cachedData.remove(item);
    }

    /**
     * Add all items to the cache
     * @param items The items to add
     */
    public void addAllItems(Set<T> items) {
        cachedData.addAll(items);
    }

    /**
     * Clear the cache
     */
    public void clearCache() {
        cachedData.clear();
    }

    protected LSReborn getPlugin() {
        return plugin;
    }
}
