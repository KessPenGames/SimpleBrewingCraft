package net.simple.brewing.craft.config;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConfigExampleItems {
    public ItemsConfigEnum oneItem() {
        ItemStack item = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Stony Stone");
        meta.setLore(List.of("Ye, it's right", "Yes"));
        item.setItemMeta(meta);
        return new ItemsConfigEnum(item, 367, List.of(Material.COBBLESTONE, Material.STONE, Material.CAKE), Color.GRAY);
    }

    public ItemsConfigEnum twoItem() {
        ItemStack item = new ItemStack(Material.COOKED_BEEF, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(List.of("Brewing on Brewing Plugin"));
        item.setItemMeta(meta);
        return new ItemsConfigEnum(item, 891, List.of(Material.BEEF, Material.MAGMA_CREAM, Material.FLINT_AND_STEEL), Color.RED);
    }

    public ItemsConfigEnum threeItem() {
        ItemStack item = new ItemStack(Material.WHITE_WOOL, 1);
        return new ItemsConfigEnum(item, 653, List.of(Material.STRING, Material.COBWEB, Material.SPIDER_EYE), Color.WHITE);
    }
}
