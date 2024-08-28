package net.simple.brewing.craft.menu;

import net.kyori.adventure.text.Component;
import net.simple.brewing.craft.config.ItemsConfigEnum;
import net.simple.brewing.craft.config.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsMenu {
    private final Map<Integer, Inventory> invList = new HashMap<>();

    private final MainConfig config;

    public ItemsMenu(MainConfig config) {
        this.config = config;
    }

    public void createInventoryPage() {
        List<ItemsConfigEnum> itemList = config.getRecipes();
        int maxMenuSlot = config.getMenuInvSize() - 9;
        boolean isEven = itemList.size() % maxMenuSlot == 0;
        int pages = isEven ? itemList.size() / maxMenuSlot : itemList.size() / maxMenuSlot + 1;
        for (int page = 1; page <= pages; page++) {
            Inventory inventory = Bukkit.createInventory(null, config.getMenuInvSize(), config.getMenuName()
                    .replace("%current_page%", page + "").replace("%max_page%", pages + ""));
            renderInventory(inventory, page);
            invList.put(page, inventory);
        }
    }

    public Inventory getInventory(int page) {
        return invList.get(page);
    }

    public void renderInventory(Inventory inventory, int page) {
        int maxSoundMenuSlot = config.getMenuInvSize() - 10;
        int maxSoundSlot = page * maxSoundMenuSlot + page - 1;
        List<ItemsConfigEnum> itemList = config.getRecipes();

        int invIndex = 0;

        for (int soundIndex = maxSoundSlot - maxSoundMenuSlot; soundIndex <= maxSoundSlot; soundIndex++) {
            if (soundIndex == itemList.size()) break;
            ItemsConfigEnum recipe = itemList.get(soundIndex);
            ItemStack item = recipe.getItem().clone();
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(recipe.getCmd());
            item.setItemMeta(meta);

            inventory.setItem(invIndex, item);

            invIndex++;
        }

        for (int index = maxSoundMenuSlot + 1; index<config.getMenuInvSize(); index++) {
            inventory.setItem(index, config.getMenuItems().blackPane);
        }

        inventory.setItem(config.getMenuInvSize() - 1, config.getMenuItems().barrier);
        inventory.setItem(config.getMenuInvSize() - 4, config.getMenuItems().nextItem);
        inventory.setItem(config.getMenuInvSize() - 6, config.getMenuItems().previouslyItem);
    }

    public boolean isNext(int page) {
        List<ItemsConfigEnum> itemList = config.getRecipes();
        int maxSoundMenuSlot = config.getMenuInvSize() - 9;
        boolean isEven = itemList.size() % maxSoundMenuSlot == 0;
        int pages = isEven ? itemList.size() / maxSoundMenuSlot : itemList.size() / maxSoundMenuSlot + 1;
        return page < pages;
    }

    public boolean isPreviously(int page) {
        return 1 < page;
    }

    public int getPageByInventory(Inventory inventory) {
        Map.Entry<Integer, Inventory> entry = invList.entrySet().stream()
                .filter(filter -> filter.getValue().equals(inventory)).findFirst().orElse(null);
        if (entry == null) return 0;
        return entry.getKey();
    }

    public boolean containsInventory(Inventory inventory) {
        return invList.containsValue(inventory);
    }
}
