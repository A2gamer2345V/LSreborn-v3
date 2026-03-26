package com.A2plugin.lsreborn.commands.MainCommand.subcommands;

import org.bukkit.command.CommandSender;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.commands.SubCommand;
import com.A2plugin.lsreborn.util.MessageUtils;
import com.A2plugin.lsreborn.util.commands.CommandUtils;

public final class ReloadSubCommand implements SubCommand {
    private final LSReborn plugin;

    public ReloadSubCommand(LSReborn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            CommandUtils.throwPermissionError(sender);
            return false;
        }

        plugin.reloadConfig();
        plugin.getLanguageManager().reload();
        plugin.getRecipeManager().registerRecipes();
        sender.sendMessage(MessageUtils.getAndFormatMsg(true, "reloadMsg", "&7Successfully reloaded the plugin!"));
        return true;
    }

    @Override
    public String getUsage() {
        return "/lsreborn reload";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("lsreborn.admin.reload");
    }
}
