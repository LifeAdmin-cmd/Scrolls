package com.lifeadmin.magic.items;

import com.lifeadmin.magic.messages.ChatError;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack scrollOfTeleportation;

    public static void init() {
        createTeleportScroll();
    }

    private static void createTeleportScroll() {
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
        item.setItemMeta(meta);
        scrollOfTeleportation = item;
    }

}

//to-do:
//meta.setDisplayName("Greater Scroll Of Teleportation")
//meta.setDisplayName("Superior Scroll Of Teleportation")