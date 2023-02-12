package com.lifeadmin.scrolls.factories;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.items.ItemManager;
import com.lifeadmin.scrolls.staticFunctions.Calcs;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ScrollLevel1Factory {
    public ItemStack createScroll() {
        ItemManager itemManager = Scrolls.getPlugin().getItemManager();
        ItemStack scroll = itemManager.scrollOfTeleportation;
        ItemMeta meta = scroll.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(Scrolls.getPlugin(), "randomNumberToIdentify"), PersistentDataType.DOUBLE, Calcs.getRandomDouble());
        scroll.setItemMeta(meta);

        return scroll;
    }
}