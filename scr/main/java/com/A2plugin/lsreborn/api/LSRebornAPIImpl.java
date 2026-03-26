package com.A2plugin.lsreborn.api;

import org.bukkit.inventory.ItemStack;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.util.customitems.customitemdata.CustomItemData;
import com.A2plugin.lsreborn.util.customitems.CustomItemManager;
import com.A2plugin.lsreborn.storage.PlayerData;

import java.util.Set;
import java.util.UUID;

public final class LSRebornAPIImpl implements LSRebornAPI {
    private final LSReborn plugin;

    public LSRebornAPIImpl(LSReborn plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public PlayerData getPlayerData(UUID uuid) {
        return plugin.getStorage().load(uuid);
    }

    @Override
    public void savePlayerData(PlayerData playerData) {
        plugin.getStorage().save(playerData);
    }

    @Override
    public boolean isEliminated(UUID uuid) {
        PlayerData playerData = plugin.getStorage().load(uuid);
        if (playerData == null) return false;
        return playerData.getMaxHealth() <= 0.0;
    }

    @Override
    public boolean eliminate(UUID uuid) {
        PlayerData playerData = plugin.getStorage().load(uuid);
        if (playerData == null) return false;
        playerData.setMaxHealth(0.0);
        plugin.getStorage().save(playerData);
        return true;
    }

    @Override
    public boolean revive(UUID uuid) {
        PlayerData playerData = plugin.getStorage().load(uuid);
        if (playerData == null) return false;
        playerData.setMaxHealth(plugin.getConfig().getInt("reviveHearts") * 2);
        playerData.setHasBeenRevived(playerData.getHasBeenRevived() + 1);
        plugin.getStorage().save(playerData);
        return true;
    }

    @Override
    public ItemStack getCustomItem(String customItemID) {
        return CustomItemManager.createCustomItem(customItemID);
    }

    @Override
    public CustomItemData getCustomItemData(String customItemID) {
        return CustomItemManager.getCustomItemData(customItemID);
    }

    @Override
    public Set<String> getCustomItemIDs() {
        return plugin.getRecipeManager().getItemIds();
    }

    @Override
    public ItemStack getDefaultHeart() {
        return CustomItemManager.createHeart();
    }

    @Override
    public String getCustomItemID(ItemStack item) {
        return CustomItemManager.getCustomItemId(item);
    }

    @Override
    public boolean isBypassActive(org.bukkit.entity.Player player) {
        return plugin.getBypassManager().hasBypass(player);
    }
}
