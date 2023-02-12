package com.lifeadmin.scrolls.factories;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.items.ItemManager;
import com.lifeadmin.scrolls.staticFunctions.Calcs;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ScrollLevel3Factory {
    public ItemStack createScroll() {
        ItemManager itemManager = Scrolls.getPlugin().getItemManager();
        ItemStack scroll = itemManager.scrollOfTeleportation;
        ItemMeta meta = scroll.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(Scrolls.getPlugin(), "randomNumberToIdentify"), PersistentDataType.DOUBLE, Calcs.getRandomDouble());

        meta.setDisplayName("§6Ultimate Scroll Of Teleportation");
        List<String> lore = meta.getLore();
        lore.set(0, "§7This perfect Scroll Of Teleportation was made");
        lore.set(1, "§7with superlative expertise and craftsmanship");
        meta.setLore(lore);

        FileConfiguration config = Scrolls.getPlugin().getConfig();
        data.set(new NamespacedKey(Scrolls.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER, 3);
        data.set(new NamespacedKey(Scrolls.getPlugin(), "maxDistance"), PersistentDataType.INTEGER, config.getInt("maxDistance_lvl3"));
        data.set(new NamespacedKey(Scrolls.getPlugin(), "coolDown"), PersistentDataType.INTEGER, config.getInt("coolDown_lvl3"));
        data.set(new NamespacedKey(Scrolls.getPlugin(), "skipWorldCheck"), PersistentDataType.INTEGER, config.getInt("skipWorldCheck_lvl3"));

        scroll.setItemMeta(meta);

        return scroll;
    }
}