package com.A2plugin.lsreborn.commands.MainCommand.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.commands.SubCommand;
import com.A2plugin.lsreborn.util.MessageUtils;
import com.A2plugin.lsreborn.util.commands.CommandUtils;

import java.util.List;

import static com.A2plugin.lsreborn.util.commands.CommandUtils.*;

public final class BypassStatusSubCommand implements SubCommand {
    private final LSReborn plugin;

    public BypassStatusSubCommand(LSReborn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            CommandUtils.throwPermissionError(sender);
            return false;
        }

        if (args.length < 2) {
            throwUsageError(sender, getUsage());
            return false;
        }

        List<OfflinePlayer> targetPlayers = parseOfflinePlayer(args[1], true, true, plugin);

        if (targetPlayers.isEmpty()) {
            sender.sendMessage(MessageUtils.getAndFormatMsg(false, "playerNotFound", "&cPlayer not found!"));
            return false;
        }

        OfflinePlayer targetPlayer = targetPlayers.get(0);
        boolean hasBypass = plugin.getBypassManager().hasBypass(targetPlayer.getPlayer());
        
        if (hasBypass) {
            sender.sendMessage(
                MessageUtils.getAndFormatMsg(
                    true,
                    "bypassStatusActive",
                    "&7Bypass permission is &aactive &7for %player%",
                    new MessageUtils.Replaceable("%player%", targetPlayer.getName())
                )
            );
        } else {
            sender.sendMessage(
                MessageUtils.getAndFormatMsg(
                    true,
                    "bypassStatusInactive",
                    "&7Bypass permission is &cinactive &7for %player%.",
                    new MessageUtils.Replaceable("%player%", targetPlayer.getName())
                )
            );
        }

        return true;
    }

    @Override
    public String getUsage() {
        return "/lsreborn checkbypass <player>";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("lsreborn.admin.bypasscheck");
    }
}
