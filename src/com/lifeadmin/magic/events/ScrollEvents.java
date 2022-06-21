package com.lifeadmin.magic.events;

import com.lifeadmin.magic.Magic;
import com.lifeadmin.magic.items.ItemManager;
import com.lifeadmin.magic.staticFunctions.Chat;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ScrollEvents implements Listener {

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                Player player = event.getPlayer();
                PersistentDataContainer data = event.getItem().getItemMeta().getPersistentDataContainer();
                if (!(data.has(new NamespacedKey(Magic.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY))) {
                    if (event.getItem().getItemMeta().equals(ItemManager.scrollOfTeleportation.getItemMeta())) {
                        Chat.chatError(player, "There was no destination set for this scroll!");
                        return;
                    }
                    return;
                }
                int[] cords = data.get(new NamespacedKey(Magic.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY);

                int x = cords[0];
                int y = cords[1];
                int z = cords[2];

                Location playerPos = player.getLocation();
                double distance = Math.sqrt(Math.pow(x - playerPos.getBlockX(), 2) + Math.pow(y - playerPos.getBlockY(), 2) + Math.pow(z - playerPos.getBlockZ(), 2));

                double maxDistance = 1500;

                World destinationWorld = Bukkit.getWorld("world");
                if (player.getWorld() == destinationWorld) {
                    if (distance <= maxDistance) {
                        Location loc = new Location(destinationWorld, x + 0.5, y, z + 0.5); // 0.5 places you in the middle of the Block
                        player.teleport(loc);
                        Chat.chatSuccess(player, "Teleport successful");
                    } else {
                        Chat.chatError(player, "You are too far away from your Destination!");
                        Chat.chatWarning(player, "Your Destination: " +  Arrays.toString(cords));
                        Chat.chatWarning(player, "Move " + Math.round(distance - maxDistance) + " blocks closer!");
                    }
                } else {
                    Chat.chatError(player, "You cannot teleport through worlds!");
                    Chat.chatWarning(player, "Destination world: " + ChatColor.AQUA +  destinationWorld.getName());
                }
            }
        }
    }

    @EventHandler
    public static void onLeftClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getItem() != null) {

                Player player = event.getPlayer();
                ItemStack item = event.getItem();
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();

                // Check if item is a scroll (unused || used), otherwise returns
                if (!(meta.equals(ItemManager.scrollOfTeleportation.getItemMeta())) &&
                        !(data.has(new NamespacedKey(Magic.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY))) return;

                Location loc = player.getTargetBlock(null, 100).getLocation();

                int[] cords = { loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ() };

                data.set(new NamespacedKey(Magic.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY, cords);
                item.setItemMeta(meta);
                Chat.chatSuccess(player, "Spawn successfully written into the scroll!");
            }
        }
    }
}
