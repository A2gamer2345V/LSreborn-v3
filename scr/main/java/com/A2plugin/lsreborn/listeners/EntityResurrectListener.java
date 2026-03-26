package com.A2plugin.lsreborn.listeners;

import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.util.customitems.CustomItemManager;

@AutoRegisterListener
public final class EntityResurrectListener implements Listener {
    private final LSReborn plugin;

    public EntityResurrectListener(LSReborn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityRessurect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack totem = null;
        if (player.getInventory().getItemInOffHand().getType().name().contains("TOTEM")) {
            totem = player.getInventory().getItemInOffHand();
        } else if (player.getInventory().getItemInMainHand().getType().name().contains("TOTEM")) {
            totem = player.getInventory().getItemInMainHand();
        }
        
        if (
                plugin.getConfig().getBoolean("preventTotems") ||
                (totem != null && CustomItemManager.isForbiddenItem(totem))
        )
            event.setCancelled(true);
    }
}
