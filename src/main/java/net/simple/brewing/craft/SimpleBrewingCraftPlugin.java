package net.simple.brewing.craft;

import net.simple.brewing.craft.commands.ItemMenuCommand;
import net.simple.brewing.craft.config.MainConfig;
import net.simple.brewing.craft.listeners.ItemBrewListener;
import net.simple.brewing.craft.listeners.MenuClickListener;
import net.simple.brewing.craft.menu.ItemsMenu;
import net.simple.brewing.craft.serializers.ColorSerializer;
import net.simple.brewing.craft.utils.SchedulerManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mikigal.config.ConfigAPI;
import pl.mikigal.config.style.CommentStyle;
import pl.mikigal.config.style.NameStyle;

public final class SimpleBrewingCraftPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        ConfigAPI.registerSerializer(Color.class, new ColorSerializer());
        MainConfig config = ConfigAPI.init(
                MainConfig.class, NameStyle.UNDERSCORE, CommentStyle.ABOVE_CONTENT, true, this
        );

        ItemsMenu menu = new ItemsMenu(config);
        menu.createInventoryPage();

        getCommand("brewingcrafts").setExecutor(new ItemMenuCommand(menu));
        Bukkit.getPluginManager().registerEvents(new ItemBrewListener(new SchedulerManager(this), config), this);
        Bukkit.getPluginManager().registerEvents(new MenuClickListener(config, menu), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
