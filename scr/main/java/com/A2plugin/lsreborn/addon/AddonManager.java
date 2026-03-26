package com.A2plugin.lsreborn.addon;

import com.A2plugin.lsreborn.LSReborn;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddonManager {
    private final LSReborn plugin;
    private final Map<String, LSRebornAddon> registeredAddons = new HashMap<>();

    public AddonManager(LSReborn plugin) {
        this.plugin = plugin;
    }

    public boolean registerAddon(LSRebornAddon addon) {
        AddonMetadata metadata = addon.getMetadata();
        
        if (registeredAddons.containsKey(metadata.getName())) {
            plugin.getLogger().warning("Addon " + metadata.getName() + " is already registered!");
            return false;
        }

        try {
            // Initialize addon config
            plugin.getConfigManager().getAddonConfig(addon);
            
            // Enable the addon
            addon.onEnable();
            addon.setEnabled(true);
            
            registeredAddons.put(metadata.getName(), addon);
            
            plugin.getLogger().info("Registered addon: " + metadata.getName() + " v" + metadata.getVersion() + " by " + metadata.getAuthor());
            
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to register addon: " + metadata.getName());
            e.printStackTrace();
            return false;
        }
    }

    public void unregisterAddon(String name) {
        LSRebornAddon addon = registeredAddons.get(name);
        if (addon != null) {
            try {
                addon.onDisable();
                addon.setEnabled(false);
                registeredAddons.remove(name);
                plugin.getLogger().info("Unregistered addon: " + name);
            } catch (Exception e) {
                plugin.getLogger().severe("Error while unregistering addon: " + name);
                e.printStackTrace();
            }
        }
    }

    public LSRebornAddon getAddon(String name) {
        return registeredAddons.get(name);
    }

    public Set<String> getRegisteredAddonNames() {
        return registeredAddons.keySet();
    }

    public void disableAllAddons() {
        for (String name : registeredAddons.keySet()) {
            unregisterAddon(name);
        }
    }
}