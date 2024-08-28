package net.simple.brewing.craft.config;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.List;

public class ItemsConfigEnum implements Serializable {
    private ItemStack item;
    private int cmd;
    private List<Material> list;
    private Color color;

    public ItemsConfigEnum() {

    }

    public ItemsConfigEnum(ItemStack item, int cmd, List<Material> list, Color color) {
        this.item = item;
        this.cmd = cmd;
        this.list = list;
        this.color = color;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public int getCmd() {
        return cmd;
    }

    public List<Material> getMaterials() {
        return list;
    }
    public Color getColor() {
        return color;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public void setList(List<Material> materials) {
        this.list = materials;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
