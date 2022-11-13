package com.lifeadmin.scrolls.events;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.inventories.UpgradeScroll;
import com.lifeadmin.scrolls.staticFunctions.Chat;
import com.lifeadmin.scrolls.staticFunctions.Experience;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class InventoryEvents implements Listener {

    private final FileConfiguration config = Scrolls.getPlugin().getConfig();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() instanceof UpgradeScroll) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                upgradeScroll(player, player.getInventory().getItemInMainHand());
            }
            else if (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                Chat.chatError(player, "Upgrade process cancelled");
                player.closeInventory();
            }
        }
    }

    private void upgradeScroll(Player player, ItemStack scroll) {
        if (scroll.getItemMeta() == null) {
            Chat.chatError(player, "You have to hold the scroll you want to upgrade in your main hand!");
            return;
        }
        ItemMeta meta = scroll.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        // Check if item is a scroll, otherwise returns
        if (!(data.has(new NamespacedKey(Scrolls.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING))) {
            Chat.chatError(player, "You have to hold the scroll you want to upgrade in your main hand!");
            return;
        };

        int level = data.get(new NamespacedKey(Scrolls.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER);
        if (level == 1) {
            int cost = config.getInt("upgradingCost_lvl2");
            if (Experience.getExp(player) < cost) {
                Chat.chatWarning(player, "You dont have enough XP to upgrade this scroll!");
                Chat.chatWarning(player, "You need " + ChatColor.GREEN + (cost - Experience.getExp(player)) + ChatColor.YELLOW + " more XP!");
                player.closeInventory();
                return;
            }
            Experience.changeExp(player, -cost);
            meta.setDisplayName("§9Greater Scroll Of Teleportation");
            List<String> lore = meta.getLore();
            lore.set(0, "§7This Scroll Of Teleportation was made");
            lore.set(1, "§7with excellent expertise and craftsmanship");
            meta.setLore(lore);
            data.set(new NamespacedKey(Scrolls.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER, 2);
            data.set(new NamespacedKey(Scrolls.getPlugin(), "maxDistance"), PersistentDataType.INTEGER, config.getInt("maxDistance_lvl2"));
            data.set(new NamespacedKey(Scrolls.getPlugin(), "coolDown"), PersistentDataType.INTEGER, config.getInt("coolDown_lvl2"));
            data.set(new NamespacedKey(Scrolls.getPlugin(), "skipWorldCheck"), PersistentDataType.INTEGER, config.getInt("skipWorldCheck_lvl2"));
        } else if (level == 2) {
            int cost = config.getInt("upgradingCost_lvl3");
            if (Experience.getExp(player) < cost) {
                Chat.chatWarning(player, "You dont have enough XP to upgrade this scroll!");
                Chat.chatWarning(player, "You need " + ChatColor.GREEN + (cost - Experience.getExp(player)) + ChatColor.YELLOW + " more XP!");
                player.closeInventory();
                return;
            }
            Experience.changeExp(player, -cost);
            meta.setDisplayName("§6Ultimate Scroll Of Teleportation");
            List<String> lore = meta.getLore();
            lore.set(0, "§7This perfect Scroll Of Teleportation was made");
            lore.set(1, "§7with superlative expertise and craftsmanship");
            meta.setLore(lore);
            data.set(new NamespacedKey(Scrolls.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER, 3);
            data.set(new NamespacedKey(Scrolls.getPlugin(), "maxDistance"), PersistentDataType.INTEGER, config.getInt("maxDistance_lvl3"));
            data.set(new NamespacedKey(Scrolls.getPlugin(), "coolDown"), PersistentDataType.INTEGER, config.getInt("coolDown_lvl3"));
            data.set(new NamespacedKey(Scrolls.getPlugin(), "skipWorldCheck"), PersistentDataType.INTEGER, config.getInt("skipWorldCheck_lvl3"));
        } else {
            Chat.chatWarning(player, "There is no upgrade for this scroll available!");
            player.closeInventory();
            return;
        }

        Bukkit.getWorld("world").playSound(player.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 8.0F, 0.5F);
        Bukkit.getWorld("world").playSound(player.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, 8.0F, 0.5F);

        scroll.setItemMeta(meta);

        Chat.chatSuccess(player, "Scroll upgraded successfully");
        player.closeInventory();
    }
}