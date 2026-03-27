package com.A2plugin.lsreborn.listeners;

import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.util.GuiManager;

import java.util.UUID;

@AutoRegisterListener
public final class InventoryCloseListener implements Listener {
    private final LSReborn plugin;

    public InventoryCloseListener(LSReborn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        plugin.getRecipeManager().cancelAnimations(event.getInventory());
        if (GuiManager.RECIPE_GUI_MAP.get(playerUUID) != null) {
            GuiManager.RECIPE_GUI_MAP.remove(playerUUID);
        }

        if (GuiManager.REVIVE_BEACON_GUI_MAP.get(playerUUID) != null) {
            GuiManager.REVIVE_BEACON_GUI_MAP.remove(playerUUID);
        }
        if (GuiManager.REVIVE_BEACON_INVENTORY_LOCATIONS.get(playerUUID) != null) {
            GuiManager.REVIVE_BEACON_INVENTORY_LOCATIONS.remove(playerUUID);
        }

        if (GuiManager.REVIVE_GUI_MAP.get(playerUUID) != null) {
            GuiManager.REVIVE_GUI_MAP.remove(playerUUID);
        }
    }
}
