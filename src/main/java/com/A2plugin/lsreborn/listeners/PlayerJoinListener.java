package com.A2plugin.lsreborn.listeners;

import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.util.MessageUtils;
import com.A2plugin.lsreborn.util.geysermc.GeyserManager;
import com.A2plugin.lsreborn.util.geysermc.GeyserPlayerFile;
import com.A2plugin.lsreborn.storage.PlayerData;
import com.A2plugin.lsreborn.storage.Storage;

@AutoRegisterListener
public final class PlayerJoinListener implements Listener {

    private final LSReborn plugin;

    private final GeyserManager geyserManager;
    private final GeyserPlayerFile geyserPlayerFile;

    public PlayerJoinListener(LSReborn plugin) {
        this.plugin = plugin;
        this.geyserManager = plugin.getGeyserManager();
        this.geyserPlayerFile = plugin.getGeyserPlayerFile();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Storage storage = plugin.getStorage();

        if(plugin.hasGeyser()) {
            if(geyserManager.isBedrockPlayer(player)) {
                geyserPlayerFile.savePlayer(player.getUniqueId(), player.getName());
            }
        }

        PlayerData playerData = loadOrCreatePlayerData(player, storage, plugin.getConfig().getInt("startHearts", 10));
        LSReborn.setMaxHealth(player, playerData.getMaxHealth());

        notifyOpAboutUpdate(player);
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

    private void notifyOpAboutUpdate(Player player) {
        if (player.isOp() && plugin.getConfig().getBoolean("checkForUpdates") && plugin.getVersionChecker().isNewVersionAvailable()) {
            player.sendMessage(MessageUtils.getAndFormatMsg(true, "newVersionAvailable", "&7A new version of lsreborn is available!\\n&c<click:OPEN_URL:https://modrinth.com/project/lsreborn/versions>https://modrinth.com/project/lsreborn/versions</click>"));
        }
    }
}