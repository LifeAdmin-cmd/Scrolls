package com.lifeadmin.magic.events;

import com.lifeadmin.magic.Magic;
import com.lifeadmin.magic.items.ItemManager;
import com.lifeadmin.magic.staticFunctions.Chat;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ScrollEvents implements Listener {
    private static boolean tpAgain = true;

    /**
     * Function to prevent a player from
     * tp again for a certain time
     * @param cooldown in miliseconds
     */
    public static void setTeleportCooldown(int cooldown) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        tpAgain = true;
                    }
                },
                cooldown
        );
    }

    @EventHandler
    public static void onRightClick(PlayerToggleSneakEvent event) {
        if (!tpAgain) {
            Chat.chatError(event.getPlayer(), "You cannot use another scroll for some time!");
            return;
        }

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

//        while (event.getPlayer().getWalkSpeed() == 0) {

            if (item.getType() != Material.AIR) {
                Player player = event.getPlayer();
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                if (!(data.has(new NamespacedKey(Magic.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY))) {
                    if (item.getItemMeta().equals(ItemManager.scrollOfTeleportation.getItemMeta())) {
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
                        player.getInventory().removeItem(item);
                        tpAgain = false;
                        ScrollEvents.setTeleportCooldown(5000);
                    } else {
                        Chat.chatError(player, "You are too far away from your Destination!");
                        Chat.chatWarning(player, "Your Destination: " +  Arrays.toString(cords));
                        Chat.chatWarning(player, "Move " + Math.round(distance - maxDistance) + " blocks closer!");
                    }
                } else {
                    Chat.chatError(player, "You cannot teleport through worlds!");
                    Chat.chatWarning(player, "Destination world: " + ChatColor.AQUA +  destinationWorld.getName());
                }
//            }
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
