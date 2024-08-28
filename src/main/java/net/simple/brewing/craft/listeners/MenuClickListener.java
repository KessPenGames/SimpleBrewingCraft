package net.simple.brewing.craft.listeners;

import net.simple.brewing.craft.config.MainConfig;
import net.simple.brewing.craft.menu.ItemsMenu;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class MenuClickListener implements Listener {
    private final MainConfig config;
    private final ItemsMenu menu;

    public MenuClickListener(MainConfig config, ItemsMenu menu) {
        this.config = config;
        this.menu = menu;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Inventory inventory = event.getView().getTopInventory();
        if (!menu.containsInventory(inventory)) return;
        event.setCancelled(true);
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(inventory)) return;
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;
        Player player = (Player) event.getWhoClicked();
        if (!player.hasPermission("simple.brewing.craft.brewingcrafts")) return;
        int page = menu.getPageByInventory(inventory);

        if (page == 0) return;

        if (config.getRecipes().stream().anyMatch(i -> currentItem.getType().equals(i.getItem().getType()))) {
            player.getInventory().addItem(currentItem);
        }

        if (currentItem.equals(config.getMenuItems().nextItem)) {
            if (!menu.isNext(page)) return;
            player.openInventory(menu.getInventory(page + 1));
        }

        if (currentItem.equals(config.getMenuItems().previouslyItem)) {
            if (!menu.isPreviously(page)) return;
            player.openInventory(menu.getInventory(page - 1));
        }

        if (currentItem.equals(config.getMenuItems().barrier)) player.closeInventory();
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        Inventory inventory = event.getView().getTopInventory();
        if (!menu.containsInventory(inventory)) return;
        Player player = (Player) event.getPlayer();
        List<Sound> soundList = Arrays.stream(Sound.values()).toList();
        soundList.forEach(player::stopSound);
    }
}
