package net.simple.brewing.craft.listeners;

import net.simple.brewing.craft.config.ItemsConfigEnum;
import net.simple.brewing.craft.config.MainConfig;
import net.simple.brewing.craft.utils.SchedulerManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemBrewListener implements Listener {
    private final Map<Location, List<Material>> ingredients = new HashMap<>();
    private final Map<Location,Boolean> isBrewing = new HashMap<>();
    private final SchedulerManager schedManager;
    private final MainConfig config;

    public ItemBrewListener(SchedulerManager schedManager, MainConfig config) {
        this.schedManager = schedManager;
        this.config = config;
    }

    @EventHandler
    public void onBrew(PlayerDropItemEvent event) {
        if (isBrewing.containsKey(event.getItemDrop().getLocation())) if (isBrewing.get(event.getItemDrop().getLocation())) return;
        schedManager.runTaskLater(() -> {
            Item item = event.getItemDrop();
            Block block = item.getLocation().getBlock();
            if (block.getType() != Material.WATER_CAULDRON) return;
            if (block.getLocation().getWorld().getBlockAt(
                    block.getX(), block.getY() - 1, block.getZ()).getType() != config.getUnderCauldronBlock()) return;
            if (block.getState().getBlockData() instanceof Levelled cauldron) {
                if (cauldron.getLevel() != cauldron.getMaximumLevel()) return;
                List<Material> list;
                if (ingredients.containsKey(block.getLocation())) {
                    list = ingredients.get(block.getLocation());
                    list.add(item.getItemStack().getType());
                    ingredients.remove(block.getLocation());
                    ingredients.put(block.getLocation(), list);
                } else {
                    list = new ArrayList<>();
                    list.add(item.getItemStack().getType());
                    ingredients.put(block.getLocation(), list);
                }
                item.remove();
                brewingItem(block.getLocation().add(0.5, 1, 0.5), item.getItemStack().getType(), list.size() - 1);
            }
        }, 20L);
    }

    public Color isTrueItem(Material material, int index) {
        for (ItemsConfigEnum item : config.getRecipes()) {
            if (item.getMaterials().size() > index) if (item.getMaterials().get(index) == material) return item.getColor();
        }
        return config.getWrongRecipeParticleColor();
    }

    public void brewingItem(Location location, Material material, int index) {
        UUID uuid = UUID.randomUUID();

        isBrewing.put(location, true);
        if (config.getEnableBrewingParticles()) {
            schedManager.runTask(uuid, () -> {
                location.getWorld().spawnParticle(Particle.DUST, location, 50, new Particle.DustOptions(
                        isTrueItem(material, index), 1.0F));
            }, 10L);
        }

        schedManager.runTaskLater(() -> {
            schedManager.stopTask(uuid);
            isBrewing.put(location, false);
            location.getWorld().playEffect(location, Effect.BREWING_STAND_BREW, 10);
        }, (long) config.getBrewingTime() * 20);
    }

    public ItemStack getReadyItem(Location location, List<Material> items) {
        for (ItemsConfigEnum dose : config.getRecipes()) {
            if (!items.equals(dose.getMaterials())) continue;
            ingredients.remove(location);
            ItemStack item = dose.getItem();
            item.editMeta(meta -> meta.setCustomModelData(dose.getCmd()));
            return item;
        }
        if (config.getEnableFailedItemCraft()) {
            ItemStack item = config.getFailItem();
            item.editMeta(meta -> meta.setCustomModelData(config.getFailItemCmd()));
            return item;
        }
        return null;
    }

    @EventHandler
    public void onInteractCauldron(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (config.getEnableItemToPickUp()) if (!event.hasItem()) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (isBrewing.containsKey(block.getLocation())) if (isBrewing.get(block.getLocation())) return;
        if (block.getState().getBlockData() instanceof Levelled cauldron) {
            if (cauldron.getLevel() != cauldron.getMaximumLevel()) return;
            if (event.getMaterial() == Material.BUCKET || event.getMaterial() == Material.GLASS_BOTTLE) {
                ingredients.remove(block.getLocation());
                return;
            }
            if (config.getEnableItemToPickUp()) if (event.getMaterial() != config.getItemToPickUp()) return;
            Player player = event.getPlayer();
            ItemStack item = getReadyItem(block.getLocation(), ingredients.get(block.getLocation()));
            if (item == null) return;
            if (config.getEnableItemToPickUp()) player.getInventory().removeItem(event.getItem());
            player.getInventory().addItem(item);
            block.setType(Material.CAULDRON);
            ingredients.remove(block.getLocation());
        }
    }
}
