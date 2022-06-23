package com.lifeadmin.magic.items;

import com.lifeadmin.magic.Magic;
import com.lifeadmin.magic.staticFunctions.Calcs;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public ItemStack scrollOfTeleportation;

    public ItemManager() {
        this.createTeleportScroll();
        this.addRecipes();
    }

    public ItemStack getScrollOfTeleportation() {
        this.createTeleportScroll();
        System.out.println("getScroll hat das hier: " + this.scrollOfTeleportation);
        return this.scrollOfTeleportation;
    }

    public ItemStack getCommandScrollOfTeleportation() {
        ItemMeta meta = scrollOfTeleportation.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(Magic.getPlugin(), "randomNumberToIdentify"), PersistentDataType.DOUBLE, Calcs.getRandomDouble());
        return this.scrollOfTeleportation;
    }

    private void createTeleportScroll() {
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

        // make scrolls identifiable
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(Magic.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING, "true");

        item.setItemMeta(meta);
        scrollOfTeleportation = item;
    }

    private void addRecipes() {
        // Shaped Recipe
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("scroll-of-teleportation"), this.scrollOfTeleportation);
        sr.shape(
                " D ",
                " M ",
                " C "
        );
        sr.setIngredient('D', Material.DIAMOND);
        sr.setIngredient('M', Material.MAP);
        sr.setIngredient('C', Material.RECOVERY_COMPASS);
        System.out.println(sr);
        Bukkit.getServer().addRecipe(sr);
    }
}

//to-do:
//meta.setDisplayName("Greater Scroll Of Teleportation")
//meta.setDisplayName("Superior Scroll Of Teleportation")