package net.simple.brewing.craft.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ItemBuilder {

    private ItemStack item;

    private ItemBuilder(ItemStack item) {
        this.item = new ItemStack(item);
    }

    public ItemStack build() {
        return build(true);
    }

    public ItemStack build(boolean color) {
        return build(color, '&');
    }

    public ItemStack build(boolean color, char colorChar) {
        if(color) applyTextViewColor(colorChar);
        return new ItemStack(item);
    }

    public ItemBuilder setType(Material type) {
        item.setType(type);
        return this;
    }

    public ItemBuilder setCount(int count) {
        item.setAmount(count);
        return this;
    }

    public ItemBuilder setName(String name) {
        return setName(Component.text(name));
    }

    public ItemBuilder setName(Component componentName) {
        return changeMeta(meta -> meta.displayName(componentName));
    }

    public ItemBuilder setLegacyLore(String... lore) {
        return setLegacyLore(Arrays.asList(lore));
    }

    public ItemBuilder setLegacyLore(Collection<String> lore) {
        return setLore(convert(lore));
    }

    public ItemBuilder setLore(Component... loreComponents) {
        return setLore(Arrays.asList(loreComponents));
    }

    public ItemBuilder setLore(Collection<Component> loreComponents) {
        return changeMeta(meta -> meta.lore(new ArrayList<>(loreComponents)));
    }

    public ItemBuilder setCustomModelData(int cmd) {
        return changeMeta(meta -> meta.setCustomModelData(cmd));
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        return changeMeta(meta -> meta.setUnbreakable(unbreakable));
    }

    public ItemBuilder setLocalizedName(String name) {
        return changeMeta(meta -> meta.setDisplayName(name));
    }

    public ItemBuilder addLore(String... lore) {
        return addLegacyLore(Arrays.asList(lore));
    }

    public ItemBuilder addLore(Component... componentLore) {
        return addLore(Arrays.asList(componentLore));
    }

    public ItemBuilder addLegacyLore(Collection<String> lore) {
        return addLore(convert(lore));
    }

    public ItemBuilder addLore(Collection<Component> components) {
        return changeMeta(meta -> {
            if (meta.hasLore()) {
                List<Component> lore = meta.lore();
                lore.addAll(components);
                meta.lore(lore);
            }
            else meta.lore(new ArrayList<>(components));
        });
    }

    public ItemBuilder addEnchant(EnchantmentComponent... enchantAttributes) {
        return addEnchants(Arrays.asList(enchantAttributes));
    }

    public ItemBuilder addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction) {
        return changeMeta(meta -> meta.addEnchant(ench, level, ignoreLevelRestriction));
    }


    public ItemBuilder addEnchants(Collection<EnchantmentComponent> enchantmentComponents) {
        return changeMeta(meta ->
                enchantmentComponents.forEach(component ->
                        meta.addEnchant(component.getEnchantment(), component.getLevel(), component.isIgnoreLevelRestriction())));
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        return changeMeta(meta -> meta.addItemFlags(itemFlags));
    }

    public ItemBuilder addAttribute(AttributeComponent... attributeComponents) {
        return addAttributes(Arrays.asList(attributeComponents));
    }

    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier attributeModifier) {
        return changeMeta(meta -> meta.addAttributeModifier(attribute, attributeModifier));
    }

    public ItemBuilder addAttributes(Collection<AttributeComponent> attributeComponents) {
        return changeMeta(meta ->
                attributeComponents.forEach(component ->
                        meta.addAttributeModifier(component.getAttribute(), component.getAttributeModifier())));
    }

    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        return changeMeta(meta -> meta.removeItemFlags(itemFlags));
    }

    public ItemBuilder removeAttribute(AttributeComponent... attributeComponents) {
        return removeAttributes(Arrays.asList(attributeComponents));
    }

    public ItemBuilder removeAttribute(Attribute attribute) {
        return changeMeta(meta -> meta.removeAttributeModifier(attribute));
    }

    public ItemBuilder removeAttribute(EquipmentSlot slot) {
        return changeMeta(meta -> meta.removeAttributeModifier(slot));
    }

    public ItemBuilder removeEnchantment(Enchantment... enchantments) {
        return removeEnchants(Arrays.asList(enchantments));
    }

    public ItemBuilder removeEnchants(Collection<Enchantment> enchantments) {
        return changeMeta(meta -> enchantments.forEach(meta::removeEnchant));
    }

    public ItemBuilder removeAttributes(Collection<AttributeComponent> attributes) {
        return changeMeta(meta ->
                attributes.forEach(component ->
                        meta.removeAttributeModifier(component.getAttribute(), component.getAttributeModifier())));
    }


    /**
     * @deprecated use {@link #updateLore(Function)}
     */
    @Deprecated
    public ItemBuilder updateLegacyLore(Function<String, String> update) {
        return changeMeta(meta -> {
            if (meta.hasLore())
                meta.setLore(meta.getLore().stream().map(update).collect(Collectors.toList()));
        });
    }

    public ItemBuilder updateLore(Function<Component, Component> update) {
        return changeMeta(meta -> {
            if (meta.hasLore())
                meta.lore(meta.lore().stream().map(update).collect(Collectors.toList()));
        });
    }

    @Deprecated
    public ItemBuilder updateLegacyTextView(Function<String, String> update) {
        updateLegacyLore(update);
        return changeMeta(meta -> meta.setDisplayName(update.apply(meta.getDisplayName())));
    }

    public ItemBuilder updateTextView(Function<Component, Component> function) {
        if(hasItemMeta()) {
            if(item.getItemMeta().hasLore()) updateLore(function);
            if(item.getItemMeta().hasDisplayName()) changeMeta(meta -> meta.displayName(function.apply(meta.displayName())));
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemMeta> ItemBuilder updateMeta(Class<T> clazz, Consumer<T> customMetaApply) {
        if(hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if(clazz.isAssignableFrom(item.getItemMeta().getClass())) customMetaApply.accept((T) meta);
            item.setItemMeta(meta);
        }
        return this;
    }

    private boolean hasItemMeta() {
        return item.getItemMeta() != null;
    }

    public ItemBuilder replacePlaceholder(String placeholder, String replace) {
        return updateTextView(component -> component.replaceText(builder -> builder.matchLiteral(placeholder).replacement(replace)));
    }

    public ItemBuilder replacePlaceholder(String placeholder, Component replace) {
        return updateTextView(component -> component.replaceText(builder -> builder.matchLiteral(placeholder).replacement(replace)));
    }

    public ItemBuilder applyTextViewColor(char replaceChar) {
        return updateTextView(component -> component.
                replaceText(builder -> builder.matchLiteral(String.valueOf(replaceChar)).replacement(String.valueOf('\u00A7'))));
    }

    public ItemBuilder applyTextViewColor() {
        return applyTextViewColor('&');
    }

    private Collection<Component> convert(Collection<String> strings) {
        List<Component> loreComponents = new ArrayList<>();
        strings.forEach(s -> loreComponents.add(Component.text(s)));
        return loreComponents;
    }

    public ItemBuilder applyIfTrue(boolean bool, UnaryOperator<ItemBuilder> update) {
        if(!bool) return this;
        return update.apply(this);
    }

    private ItemBuilder changeMeta(Consumer<ItemMeta> metaEdit) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            metaEdit.accept(meta);
            item.setItemMeta(meta);
        }
        return this;
    }


    public static class EnchantmentComponent {
        private final Enchantment enchantment;
        private final int level;
        private final boolean ignoreLevelRestriction;

        public EnchantmentComponent(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
            this.enchantment = enchantment;
            this.level = level;
            this.ignoreLevelRestriction = ignoreLevelRestriction;
        }

        public EnchantmentComponent(Enchantment enchantment) {
            this(enchantment, 0);
        }

        public EnchantmentComponent(Enchantment enchantment, int level) {
            this(enchantment, level, true);
        }

        public int getLevel() {
            return level;
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public boolean isIgnoreLevelRestriction() {
            return ignoreLevelRestriction;
        }
    }

    public static class AttributeComponent {
        private final Attribute attribute;
        private final AttributeModifier attributeModifier;

        public AttributeComponent(Attribute attribute, AttributeModifier attributeModifier) {
            this.attribute = attribute;
            this.attributeModifier = attributeModifier;
        }

        public Attribute getAttribute() {
            return attribute;
        }

        public AttributeModifier getAttributeModifier() {
            return attributeModifier;
        }
    }


    public static AttributeComponent asComponent(Attribute attribute, AttributeModifier attributeModifier) {
        return new AttributeComponent(attribute, attributeModifier);
    }

    public static EnchantmentComponent asComponent(Enchantment enchantment) {
        return asComponent(enchantment, 0);
    }

    public static EnchantmentComponent asComponent(Enchantment enchantment, int level) {
        return asComponent(enchantment, level, true);
    }

    public static EnchantmentComponent asComponent(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        return new EnchantmentComponent(enchantment, level, ignoreLevelRestriction);
    }

    public static ItemBuilder builder() {
        return ItemBuilder.builder(Material.AIR);
    }

    public static ItemBuilder builder(Material type) {
        return ItemBuilder.builder(new ItemStack(type));
    }

    public static ItemBuilder builder(ItemStack item) {
        return new ItemBuilder(item);
    }

}