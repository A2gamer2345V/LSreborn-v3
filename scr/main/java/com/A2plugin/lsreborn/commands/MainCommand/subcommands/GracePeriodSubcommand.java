package com.A2plugin.lsreborn.commands.MainCommand.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.commands.SubCommand;
import com.A2plugin.lsreborn.util.MessageUtils;
import com.A2plugin.lsreborn.util.commands.CommandUtils;

import java.util.List;

import static com.A2plugin.lsreborn.util.commands.CommandUtils.*;

public final class GracePeriodSubcommand implements SubCommand {
    private final LSReborn plugin;

    public GracePeriodSubcommand(LSReborn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            CommandUtils.throwPermissionError(sender);
            return false;
        }

        if (args.length < 3) {
            throwUsageError(sender, getUsage());
            return false;
        }

        List<OfflinePlayer> targetPlayers = parseOfflinePlayer(args[1], true, true, plugin);

        if (targetPlayers.isEmpty()) {
            sender.sendMessage(MessageUtils.getAndFormatMsg(false, "playerNotFound", "&cPlayer not found!"));
            return false;
        }

        String secondArg = args[2].toLowerCase();

        switch (secondArg) {
            case "skip":
                handleGracePeriod(sender, targetPlayers, GracePeriodAction.SKIP);
                break;
            case "reset":
                handleGracePeriod(sender, targetPlayers, GracePeriodAction.RESET);
                break;
            default:
                throwUsageError(sender, getUsage());
                return false;
        }

        return true;
    }

    private void handleGracePeriod(CommandSender sender, List<OfflinePlayer> targetPlayers, GracePeriodAction gracePeriodAction) {
        int successCount = 0;
        OfflinePlayer lastSucessfulPlayer = null;

        for (OfflinePlayer targetPlayer : targetPlayers) {
            boolean success = gracePeriodAction == GracePeriodAction.SKIP ?
                    plugin.getGracePeriodManager().skipGracePeriod(targetPlayer) :
                    plugin.getGracePeriodManager().resetGracePeriod(targetPlayer);

            if (success) {
                successCount++;
                lastSucessfulPlayer = targetPlayer;
            }
        }

        if (successCount == 0 || successCount > 1) {
            sender.sendMessage(
                    gracePeriodAction == GracePeriodAction.SKIP ?
                            MessageUtils.getAndFormatMsg(
                                    true,
                                    "gracePeriodSkipSuccess",
                                    "&7Successfully skipped the grace period for &c%playerCount% &7players.",
                                    new MessageUtils.Replaceable("%playerCount%", String.valueOf(successCount))
                            ) : MessageUtils.getAndFormatMsg(
                            true,
                                "gracePeriodResetSuccess",
                                "&7Successfully reset the grace period for &c%playerCount% &7players.",
                                new MessageUtils.Replaceable("%playerCount%", String.valueOf(successCount))
                            )
            );
        } else {
            sender.sendMessage(
                    gracePeriodAction == GracePeriodAction.SKIP ?
                            MessageUtils.getAndFormatMsg(
                                    true,
                                    "gracePeriodSkipSuccessOnePlayer",
                                    "&7Successfully skipped the grace period for &c%player%&7.",
                                    new MessageUtils.Replaceable("%player%", lastSucessfulPlayer.getName())
                            ) : MessageUtils.getAndFormatMsg(
                            true,
                                "gracePeriodResetSuccessOnePlayer",
                                "&7Successfully reset the grace period for &c%player%&7.",
                                new MessageUtils.Replaceable("%player%", lastSucessfulPlayer.getName())
                            )
            );
        }
    }

    @Override
    public String getUsage() {
        return "/lsreborn graceperiod <player> <skip | reset>";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("lsreborn.admin.graceperiod");
    }

    private enum GracePeriodAction {
        SKIP,
        RESET
    }
}