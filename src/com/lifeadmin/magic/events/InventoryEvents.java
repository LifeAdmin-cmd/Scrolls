package com.lifeadmin.magic.events;

import com.lifeadmin.magic.Magic;
import com.lifeadmin.magic.inventories.UpgradeScroll;
import com.lifeadmin.magic.staticFunctions.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class InventoryEvents implements Listener {

    private final FileConfiguration config = Magic.getPlugin().getConfig();

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
        if (!(data.has(new NamespacedKey(Magic.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING))) {
            Chat.chatError(player, "You have to hold the scroll you want to upgrade in your main hand!");
            return;
        };

        int level = data.get(new NamespacedKey(Magic.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER);
        if (level == 1) {
            int cost = config.getInt("upgradingCost_lvl2");
            if (player.getTotalExperience() < cost) {
                Chat.chatWarning(player, "You dont have enough XP to upgrade this scroll!");
                Chat.chatWarning(player, "You need " + ChatColor.GREEN + (cost - player.getTotalExperience()) + ChatColor.YELLOW + " more XP!");
                player.closeInventory();
                return;
            }
            meta.setDisplayName("§9Greater Scroll Of Teleportation");
            List<String> lore = meta.getLore();
            lore.set(0, "§7This Scroll Of Teleportation was made");
            lore.set(1, "§7with excellent expertise and craftsmanship");
            meta.setLore(lore);
            data.set(new NamespacedKey(Magic.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER, 2);
            data.set(new NamespacedKey(Magic.getPlugin(), "maxDistance"), PersistentDataType.INTEGER, config.getInt("maxDistance_lvl2"));
            data.set(new NamespacedKey(Magic.getPlugin(), "coolDown"), PersistentDataType.INTEGER, config.getInt("coolDown_lvl2"));
            data.set(new NamespacedKey(Magic.getPlugin(), "skipWorldCheck"), PersistentDataType.INTEGER, config.getInt("skipWorldCheck_lvl2"));
        } else if (level == 2) {
            int cost = config.getInt("upgradingCost_lvl3");
            if (player.getTotalExperience() < cost) {
                Chat.chatWarning(player, "You dont have enough XP to upgrade this scroll!");
                Chat.chatWarning(player, "You need " + ChatColor.GREEN + (cost - player.getTotalExperience()) + ChatColor.YELLOW + " more XP!");
                player.closeInventory();
                return;
            }
            meta.setDisplayName("§6Ultimate Scroll Of Teleportation");
            List<String> lore = meta.getLore();
            lore.set(0, "§7This perfect Scroll Of Teleportation was made");
            lore.set(1, "§7with superlative expertise and craftsmanship");
            meta.setLore(lore);
            data.set(new NamespacedKey(Magic.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER, 3);
            data.set(new NamespacedKey(Magic.getPlugin(), "maxDistance"), PersistentDataType.INTEGER, config.getInt("maxDistance_lvl3"));
            data.set(new NamespacedKey(Magic.getPlugin(), "coolDown"), PersistentDataType.INTEGER, config.getInt("coolDown_lvl3"));
            data.set(new NamespacedKey(Magic.getPlugin(), "skipWorldCheck"), PersistentDataType.INTEGER, config.getInt("skipWorldCheck_lvl3"));
        } else {
            Chat.chatWarning(player, "There is no upgrade for this scroll available!");
            player.closeInventory();
            return;
        }

        scroll.setItemMeta(meta);

        Chat.chatSuccess(player, "Scroll upgraded successfully");
        player.closeInventory();
    }
}
