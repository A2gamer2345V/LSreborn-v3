package com.A2plugin.lsreborn.util;

import org.bukkit.entity.Player;
import com.A2plugin.lsreborn.LSReborn;

public final class BypassManager {
    private final LSReborn plugin;

    public BypassManager(LSReborn plugin) {
        this.plugin = plugin;
    }

    public BypassConfig getConfig() {
        return new BypassConfig(plugin);
    }

    public boolean isEnabled() {
        return getConfig().isEnabled();
    }

    public boolean hasBypass(Player player) {
        if (!isEnabled() || player == null) return false;
        return player.hasPermission("lsreborn.bypass");
    }

    public static class BypassConfig {
        private final LSReborn plugin;

        public BypassConfig(LSReborn plugin) {
            this.plugin = plugin;
        }

        public boolean isEnabled() {
            return plugin.getConfig().getBoolean("bypassPermission.enabled", false);
        }

        public boolean damageFromPlayers() {
            return plugin.getConfig().getBoolean("bypassPermission.damageFromPlayers", false);
        }

        public boolean damageToPlayers() {
            return plugin.getConfig().getBoolean("bypassPermission.damageToPlayers", false);
        }

        public boolean useHearts() {
            return plugin.getConfig().getBoolean("bypassPermission.useHearts", false);
        }

        public boolean looseHearts() {
            return plugin.getConfig().getBoolean("bypassPermission.looseHearts", false);
        }

        public boolean gainHearts() {
            return plugin.getConfig().getBoolean("bypassPermission.gainHearts", false);
        }
    }
}
