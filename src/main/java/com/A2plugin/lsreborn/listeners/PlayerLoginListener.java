package com.A2plugin.lsreborn.listeners;

import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.util.MessageUtils;
import com.A2plugin.lsreborn.storage.PlayerData;
import com.A2plugin.lsreborn.storage.Storage;

@AutoRegisterListener
public final class PlayerLoginListener implements Listener {
    private final LSReborn plugin;

    public PlayerLoginListener(LSReborn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Storage storage = plugin.getStorage();

        PlayerData playerData = loadOrCreatePlayerData(player, storage, plugin.getConfig().getInt("startHearts", 10));

        if (shouldKickPlayer(playerData)) {
            kickPlayer(event);
        }
    }

    private PlayerData loadOrCreatePlayerData(Player player, Storage storage, int startHearts) {
        PlayerData playerData = plugin.getStorage().load(player.getUniqueId());
        if (playerData == null) {
            playerData = new PlayerData(player.getName(), player.getUniqueId());
            playerData.setFirstJoin(System.currentTimeMillis());
            playerData.setMaxHealth(startHearts * 2.0);
            storage.save(playerData);
            plugin.getGracePeriodManager().startGracePeriod(player);
            plugin.getOfflinePlayerCache().addItem(player.getName());
        }
        return playerData;
    }

    private boolean shouldKickPlayer(PlayerData playerData) {
        boolean disabledBanOnDeath = plugin.getConfig().getBoolean("disablePlayerBanOnElimination");
        double minHearts = plugin.getConfig().getInt("minHearts") * 2;
        return playerData.getMaxHealth() <= minHearts && !disabledBanOnDeath;
    }

    private void kickPlayer(PlayerLoginEvent event) {
        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        Component kickmsg = MessageUtils.getAndFormatMsg(false, "eliminatedJoin", "&cYou don't have any hearts left!");
        event.kickMessage(kickmsg);
    }
}