package com.lifeadmin.scrolls.items;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.staticFunctions.Calcs;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import com.lifeadmin.scrolls.items.ItemFactory;

public class ItemManager {

    public ItemStack scrollOfTeleportation;

    public ItemManager() {
        this.scrollOfTeleportation = ItemFactory.createTeleportScroll();
        this.addRecipes();
    }

    private final FileConfiguration config = Scrolls.getPlugin().getConfig();

    public ItemStack getScrollOfTeleportation() {
        return this.scrollOfTeleportation;
    }

    private void addRecipes() {
        // Shaped Recipe
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("scroll-of-teleportation"), this.scrollOfTeleportation);
        sr.shape(
                "EDE",
                "PMP",
                "ECE"
        );
        sr.setIngredient('D', Material.DIAMOND);
        sr.setIngredient('M', Material.MAP);
        sr.setIngredient('P', Material.PAPER);
        sr.setIngredient('C', Material.RECOVERY_COMPASS);
        sr.setIngredient('E', Material.ENDER_PEARL);
        Bukkit.getServer().addRecipe(sr);
    }
}

//to-do:
//meta.setDisplayName("Greater Scroll Of Teleportation")
//meta.setDisplayName("Superior Scroll Of Teleportation")