package com.A2plugin.lsreborn.util.geysermc;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import com.A2plugin.lsreborn.LSReborn;

import java.util.UUID;

public class GeyserManager {
    private final FloodgateApi geyserApi = FloodgateApi.getInstance();
    private final GeyserPlayerFile geyserPlayerFile = LSReborn.getInstance().getGeyserPlayerFile();

    public boolean isBedrockPlayer(Player player) {
        return geyserApi.isFloodgatePlayer(player.getUniqueId());
    }

    public UUID getOfflineBedrockPlayerUniqueId(String playerName) {
        return geyserPlayerFile.getPlayerUUID(playerName);
    }

    public String getOfflineBedrockPlayerName(UUID playerUniqueId) {
        return geyserPlayerFile.getPlayerName(playerUniqueId);
    }
}