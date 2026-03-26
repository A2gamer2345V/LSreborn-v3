package com.A2plugin.lsreborn.commands.MainCommand.subcommands;

import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.storage.PlayerData;
import com.zetaplugins.zetacore.services.commands.AutoRegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@AutoRegisterCommand(command = "lsreborn", subCommand = "resethearts")
public class ResetHeartsSubCommand {

    private final LSReborn plugin = LSReborn.getInstance();

    public void execute(CommandSender sender, String[] args) {

        if (!sender.hasPermission("lsreborn.admin.resethearts")) {
            sender.sendMessage("§cNo permission.");
            return;
        }

        int startHearts = plugin.getConfig().getInt("startHearts");

        for (UUID uuid : plugin.getStorage().getAllPlayerUUIDs()) {

            PlayerData data = plugin.getStorage().load(uuid);

            data.setMaxHealth(startHearts * 2);
            plugin.getStorage().save(data);

            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {

                player.getAttribute(Attribute.MAX_HEALTH)
                        .setBaseValue(startHearts * 2);

                if (player.getHealth() > startHearts * 2)
                    player.setHealth(startHearts * 2);
            }
        }

        sender.sendMessage("§eAll player hearts reset.");
    }
}