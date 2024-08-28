package net.simple.brewing.craft.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SchedulerManager {
    private Map<UUID, BukkitTask> map = new HashMap<>();
    private final Plugin plugin;

    public SchedulerManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void runTaskLater(Runnable runnable, Long cooldown) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, cooldown);
    }

    public void runTask(UUID uuid, Runnable runnable, long delay) {
        map.put(uuid, Bukkit.getScheduler().runTaskTimer(plugin, runnable, 0, delay));
    }

    public void stopTask(UUID uuid) {
        if (!map.containsKey(uuid)) return;
        map.get(uuid).cancel();
        map.remove(uuid);
    }
}
