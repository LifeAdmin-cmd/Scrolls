package com.lifeadmin.scrolls.items;

import com.lifeadmin.scrolls.Scrolls;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {
    public static ItemStack createTeleportScroll(){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§5Scroll Of Teleportation");
        List<String> lore = new ArrayList<>();
        lore.add("§7This Scroll Of Teleportation was made");
        lore.add("§7with good expertise and craftsmanship");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        FileConfiguration config = Scrolls.getPlugin().getConfig();

        // make scrolls identifiable and set variables from config
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(Scrolls.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING, "true");
        data.set(new NamespacedKey(Scrolls.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER, 1);
        data.set(new NamespacedKey(Scrolls.getPlugin(), "maxDistance"), PersistentDataType.INTEGER, config.getInt("maxDistance"));
        data.set(new NamespacedKey(Scrolls.getPlugin(), "coolDown"), PersistentDataType.INTEGER, config.getInt("coolDown"));
        data.set(new NamespacedKey(Scrolls.getPlugin(), "skipWorldCheck"), PersistentDataType.INTEGER, config.getInt("skipWorldCheck"));

        item.setItemMeta(meta);
        return item;
    }
}
