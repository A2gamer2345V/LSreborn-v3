package com.A2plugin.lsreborn.commands.MainCommand.subcommands;

import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.storage.PlayerData;
import com.zetaplugins.zetacore.services.commands.AutoRegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AutoRegisterCommand(command = "lsreborn", subCommand = "set")
public class SetHeartsSubCommand {

    private final LSReborn plugin = LSReborn.getInstance();

    public void execute(CommandSender sender, String[] args) {

        if (!sender.hasPermission("lsreborn.admin.set")) {
            sender.sendMessage("§cNo permission.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lsreborn set <player> <amount>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        int hearts;

        try {
            hearts = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage("§cInvalid number.");
            return;
        }

        PlayerData data = plugin.getStorage().load(target.getUniqueId());

        if (data == null) {
            sender.sendMessage("§cPlayer data not found.");
            return;
        }

        data.setMaxHealth(hearts * 2);
        plugin.getStorage().save(data);

        if (target.isOnline()) {

            Player player = target.getPlayer();

            player.getAttribute(Attribute.MAX_HEALTH)
                    .setBaseValue(hearts * 2);

            if (player.getHealth() > hearts * 2)
                player.setHealth(hearts * 2);
        }

        sender.sendMessage("§aSet hearts for §e" + target.getName());
    }
}