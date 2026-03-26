package com.A2plugin.lsreborn.commands.MainCommand.subcommands;

import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.storage.PlayerData;
import com.zetaplugins.zetacore.services.commands.AutoRegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AutoRegisterCommand(command = "lsreborn", subCommand = "take")
public class TakeHeartsSubCommand {

    private final LSReborn plugin = LSReborn.getInstance();

    public void execute(CommandSender sender, String[] args) {

        if (!sender.hasPermission("lsreborn.admin.take")) {
            sender.sendMessage("§cNo permission.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lsreborn take <player> <amount>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage("§cInvalid number.");
            return;
        }

        PlayerData data = plugin.getStorage().load(target.getUniqueId());

        int current = (int) (data.getMaxHealth() / 2);
        int newHearts = Math.max(0, current - amount);

        data.setMaxHealth(newHearts * 2);
        plugin.getStorage().save(data);

        if (target.isOnline()) {

            Player player = target.getPlayer();

            player.getAttribute(Attribute.MAX_HEALTH)
                    .setBaseValue(newHearts * 2);

            if (player.getHealth() > newHearts * 2)
                player.setHealth(newHearts * 2);
        }

        sender.sendMessage("§cRemoved hearts from §e" + target.getName());
    }
}