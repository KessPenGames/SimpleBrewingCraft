package net.simple.brewing.craft.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ColorText {
    public static String getColor(String color) {
        return color.replace("&", "\u00a7");
    }

    public static String getColored(String string) {
        if(string == null) return null;
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> getColored(List<String> list) {
        return list.stream().map(ColorText::getColored).collect(Collectors.toList());
    }

    public static String[] getColored(String[] args) {
        return Arrays.stream(args).map(ColorText::getColored).toArray(String[]::new);
    }
}
