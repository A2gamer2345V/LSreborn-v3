package com.A2plugin.lsreborn.commands.MainCommand.subcommands;

import org.bukkit.command.CommandSender;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.commands.SubCommand;
import com.A2plugin.lsreborn.util.MessageUtils;
import com.A2plugin.lsreborn.util.commands.CommandUtils;

public final class HelpSubCommand implements SubCommand {
    private final LSReborn plugin;

    public HelpSubCommand(LSReborn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            CommandUtils.throwPermissionError(sender);
            return false;
        }

        StringBuilder helpMessage = new StringBuilder("<reset><!i><!b> \n&8----------------------------------------------------\n&c&llsreborn &7help page<!b>\n&8----------------------------------------------------\n");
        addHelpEntry(helpMessage, sender, "lsreborn.admin.reload", "/lsreborn reload", "- reload the config");
        addHelpEntry(helpMessage, sender, "lsreborn.admin.setlife", "/lsreborn hearts", "- modify how many hearts a player has");
        addHelpEntry(helpMessage, sender, "lsreborn.admin.giveitem", "/lsreborn giveItem", "- give other players custom items, such as hearts");
        addHelpEntry(helpMessage, sender, "lsreborn.viewrecipes", "/lsreborn recipe", "- view all recipes");
        addHelpEntry(helpMessage, sender, "lsreborn.admin.graceperiod", "/lsreborn graceperiod", "- manage a player's grace period");
        addHelpEntry(helpMessage, sender, "lsreborn.managedata", "/lsreborn data", "- import, export or edit player data");
        addHelpEntry(helpMessage, sender, "lsreborn.admin.revive", "/revive", "- revive a player without a revive item");
        addHelpEntry(helpMessage, sender, "lsreborn.admin.eliminate", "/eliminate", "- eliminate a player");
        addHelpEntry(helpMessage, sender, "lsreborn.withdraw", "/withdrawheart", "- withdraw a heart");
        addHelpEntry(helpMessage, sender, "lsreborn.viewhearts", "/hearts", "- view your hearts or the hearts of another player");
        helpMessage.append("\n&8----------------------------------------------------\n<reset><!i><!b> ");

        sender.sendMessage(MessageUtils.formatMsg(helpMessage.toString()));
        return true;
    }

    private void addHelpEntry(StringBuilder helpMessage, CommandSender sender, String permission, String command, String description) {
        if (sender.hasPermission(permission)) {
            helpMessage
                    .append("&c<click:SUGGEST_COMMAND:")
                    .append(command)
                    .append(">")
                    .append(command)
                    .append("</click> &8- &7")
                    .append(description)
                    .append("\n");
        }
    }

    @Override
    public String getUsage() {
        return "/lsreborn help";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("lsreborn.help");
    }
}
