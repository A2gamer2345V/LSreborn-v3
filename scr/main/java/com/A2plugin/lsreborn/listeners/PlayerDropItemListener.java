package com.A2plugin.lsreborn.listeners;

import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import com.A2plugin.lsreborn.util.customitems.CustomItemManager;

@AutoRegisterListener
public class PlayerDropItemListener implements Listener {
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        if (CustomItemManager.isForbiddenItem(item.getItemStack())) item.remove();
    }
}
