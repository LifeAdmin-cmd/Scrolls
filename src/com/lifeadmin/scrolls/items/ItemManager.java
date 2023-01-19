package com.lifeadmin.scrolls.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import com.lifeadmin.scrolls.items.ItemFactory;

public class ItemManager {
    public ItemStack scrollOfTeleportation;

    public ItemManager() {
        this.scrollOfTeleportation = ItemFactory.createTeleportScroll();
        this.addRecipes();
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
