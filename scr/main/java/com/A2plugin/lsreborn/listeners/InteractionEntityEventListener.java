package com.A2plugin.lsreborn.listeners;

import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.util.MessageUtils;
import com.A2plugin.lsreborn.util.customitems.CustomItemManager;

@AutoRegisterListener
public final class InteractionEntityEventListener implements Listener {
    private final LSReborn plugin;

    public InteractionEntityEventListener(LSReborn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteractionEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) item = event.getPlayer().getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        Entity targetEntity = event.getRightClicked();

        boolean preventItemFrames = plugin.getConfig().getBoolean("preventCustomItemsInItemFrames");

        if (
                preventItemFrames &&
                        (CustomItemManager.isHeartItem(item) || CustomItemManager.isReviveItem(item))
                && targetEntity.getType().equals(EntityType.ITEM_FRAME)
        ) {
            event.setCancelled(true);
            player.sendMessage(MessageUtils.getAndFormatMsg(false, "itemFramesDisabled", "&cYou cannot put custom items in itemframes!"));
        }
    }
}
