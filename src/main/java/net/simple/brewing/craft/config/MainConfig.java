package net.simple.brewing.craft.config;

import net.simple.brewing.craft.utils.ColorText;
import net.simple.brewing.craft.utils.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.Comment;
import pl.mikigal.config.annotation.ConfigName;

import java.io.Serializable;
import java.util.List;

@ConfigName("config.yml")
public interface MainConfig extends Config {
    @Comment("Brewing time (in seconds)")
    default int getBrewingTime() {
        return 30;
    }

    @Comment("Enable/Disable Brewing Particles")
    default boolean getEnableBrewingParticles() {
        return true;
    }

    @Comment("Wrong Recipe Particle Color")
    default Color getWrongRecipeParticleColor() {
        return Color.GRAY;
    }

    @Comment("Enable/Disable Item for a failed brewing crafting attempt")
    default boolean getEnableFailedItemCraft() {
        return true;
    }

    @Comment("Enable/Disable Item to pick up the item after brewing (It will be removed after collecting the finished item)")
    default boolean getEnableItemToPickUp() {
        return true;
    }

    @Comment("Item to pick up the item after brewing")
    default Material getItemToPickUp() {
        return Material.BOWL;
    }

    @Comment("Block under cauldron for brewing crafting")
    default Material getUnderCauldronBlock() {
        return Material.CAMPFIRE;
    }

    @Comment("Name menu with all brewing items")
    default String getMenuName() {
        return "Menu Brewing Items %current_page%/%max_page%";
    }

    @Comment("Inventory Size menu with all brewing items")
    default int getMenuInvSize() {
        return 54;
    }

    @Comment("Items In Menu with all brewing items")
    default Items getMenuItems() {
        return new Items();
    }

    @Comment("Fail item on brewing")
    default ItemStack getFailItem() {
        ItemStack item = new ItemStack(Material.DIRT, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Dirty Dirt");
        item.setItemMeta(meta);
        return item;
    }

    @Comment("Custom Model Data for fail item on brewing")
    default int getFailItemCmd() {
        return 178;
    }

    @Comment("Recipes for brewing crafting")
    default List<ItemsConfigEnum> getRecipes() {
        ConfigExampleItems items = new ConfigExampleItems();
        return List.of(items.oneItem(), items.twoItem(), items.threeItem());
    }

    class Items implements Serializable {
        public ItemStack blackPane = ItemBuilder.builder(Material.BLACK_STAINED_GLASS_PANE).setName(ColorText.getColor(" ")).addLore(ColorText.getColor(" ")).build();
        public ItemStack barrier = ItemBuilder.builder(Material.BARRIER).setName(ColorText.getColor("&4Close menu")).addLore(ColorText.getColor(" ")).build();
        public ItemStack nextItem = ItemBuilder.builder(Material.PAPER).setName(ColorText.getColor("&eNext page")).addLore(ColorText.getColor(" ")).build();
        public ItemStack previouslyItem = ItemBuilder.builder(Material.PAPER).setName(ColorText.getColor("&ePrevious page")).addLore(ColorText.getColor(" ")).build();

        public Items() {

        }
    }
}
