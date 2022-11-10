package com.lifeadmin.magic.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpgradeScroll implements InventoryHolder {

    public Inventory inv;

    public UpgradeScroll() {
        inv = Bukkit.createInventory(this, 9, "Confirm Upgrade");
        init();
    }

    private void init() {
        ItemStack item;

        // Left accept items
        for (int i = 0; i < 4; i++) {
            item = createItem("§a§lAccept", Material.LIME_STAINED_GLASS_PANE, Collections.singletonList("§7Accepts upgrade"));
            inv.setItem(i, item);
        }

        // Center book item
        List<String> lore = new ArrayList<>();
        lore.add(("§7Please either accept or"));
        lore.add(("§7deny the upgrade request"));
        item = createItem("§b§lUpgrade Scroll", Material.BOOK, lore);
        inv.setItem(inv.firstEmpty(), item);

        // Right deny items
        for (int i = 5; i < 9; i++) {
            item = createItem("§c§lDeny", Material.RED_STAINED_GLASS_PANE, Collections.singletonList("§7Denies upgrade"));
            inv.setItem(i, item);
        }
    }

    private ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
