package com.A2plugin.lsreborn.commands.MainCommand.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.A2plugin.lsreborn.LSReborn;
import com.A2plugin.lsreborn.commands.SubCommand;
import com.A2plugin.lsreborn.util.MessageUtils;
import com.A2plugin.lsreborn.util.commands.CommandUtils;

import static com.A2plugin.lsreborn.util.commands.CommandUtils.throwUsageError;

public final class RecipeSubCommand implements SubCommand {
    private final LSReborn plugin;

    public RecipeSubCommand(LSReborn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            CommandUtils.throwPermissionError(sender);
            return false;
        }

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (args.length < 2) {
            throwUsageError(player, getUsage());
            return false;
        }

        String itemId = args[1];

        if (itemId == null || !plugin.getRecipeManager().getItemIds().contains(itemId)) {
            throwUsageError(player, getUsage());
            return false;
        }

        if (!plugin.getRecipeManager().isCraftable(itemId)) {
            player.sendMessage(MessageUtils.getAndFormatMsg(
                    false,
                    "recipeNotCraftable",
                    "&cThis item is not craftable!"
            ));
            return false;
        }

        String recipeId = args.length > 2 ? args[2] : null;

        if (recipeId == null) {
            plugin.getRecipeManager().renderRecipe(player, itemId);
            return true;
        }

        plugin.getRecipeManager().renderRecipe(player, itemId, recipeId);
        return true;
    }

    @Override
    public String getUsage() {
        return "/lsreborn recipe <" + String.join(" | ", plugin.getRecipeManager().getItemIds()) + "> [<recipeId>]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("lsreborn.viewrecipes");
    }
}
