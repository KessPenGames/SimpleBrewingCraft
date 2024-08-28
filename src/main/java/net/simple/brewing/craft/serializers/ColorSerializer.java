package net.simple.brewing.craft.serializers;

import org.bukkit.Color;
import pl.mikigal.config.BukkitConfiguration;
import pl.mikigal.config.serializer.Serializer;

import java.util.HashMap;
import java.util.Map;

public class ColorSerializer extends Serializer<Color> {
    @Override
    protected void saveObject(String s, Color color, BukkitConfiguration bukkitConfiguration) {
        Map<String, Object> serialized = color.serialize();
        bukkitConfiguration.set(s + ".color.ALPHA", serialized.get("ALPHA"));
        bukkitConfiguration.set(s + ".color.RED", serialized.get("RED"));
        bukkitConfiguration.set(s + ".color.BLUE", serialized.get("BLUE"));
        bukkitConfiguration.set(s + ".color.GREEN", serialized.get("GREEN"));
    }

    @Override
    public Color deserialize(String s, BukkitConfiguration bukkitConfiguration) {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("ALPHA", bukkitConfiguration.getObject(s + ".color.ALPHA", Object.class));
        serialized.put("RED", bukkitConfiguration.getObject(s + ".color.RED", Object.class));
        serialized.put("BLUE", bukkitConfiguration.getObject(s + ".color.BLUE", Object.class));
        serialized.put("GREEN", bukkitConfiguration.getObject(s + ".color.GREEN", Object.class));
        return Color.deserialize(serialized);
    }
}
