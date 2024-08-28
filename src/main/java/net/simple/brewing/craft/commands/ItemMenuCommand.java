package net.simple.brewing.craft.commands;

import net.simple.brewing.craft.menu.ItemsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemMenuCommand implements CommandExecutor {
    private final ItemsMenu menu;

    public ItemMenuCommand(ItemsMenu menu) {
        this.menu = menu;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (!player.hasPermission("simple.brewing.craft.brewingcrafts")) return true;
            player.openInventory(menu.getInventory(1));
        }
        return true;
    }
}
