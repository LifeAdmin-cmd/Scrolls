package com.lifeadmin.scrolls.events;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.inventories.UpgradeScroll;
import com.lifeadmin.scrolls.staticFunctions.Calcs;
import com.lifeadmin.scrolls.staticFunctions.Chat;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ScrollEvents implements Listener {

    public ScrollEvents() {  }
    public static HashMap<UUID, LocalDateTime> coolDownArray = new HashMap<>();

    @EventHandler
    public void startTeleportEvent(PlayerToggleSneakEvent event) {
        if (!(event.getPlayer().isSneaking())) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        // Checks if player is holding a scroll
        if (item.getType() != Material.AIR) {
            Player player = event.getPlayer();
            TeleportCommand teleportCommand = new TeleportCommand(item, player);
            if (!teleportCommand.isValidScroll()) {
                return;
            } else if (!teleportCommand.hasValidCords()) {
                Chat.chatError(player, "There was no destination set for this scroll!");
                return;
            }

            // Checks if the player is standing on a block, e.g. if player is moving
            Material blockUnderPlayer = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
            if (blockUnderPlayer == Material.AIR || blockUnderPlayer == Material.CAVE_AIR || blockUnderPlayer == Material.VOID_AIR) {
                Chat.chatError(player, "You cannot use this scroll while moving!");
                return;
            }

            // Adds cooldown to player for using the scroll if he doesn't have a cooldown yet
            UUID id = player.getUniqueId();
            coolDownArray.putIfAbsent(id, LocalDateTime.now());
            LocalDateTime coolDownDateTime = coolDownArray.get(id);

            // Checks if cooldown ended
            if (LocalDateTime.now().isBefore(coolDownDateTime)) {
                Chat.chatError(event.getPlayer(), "You have to wait: " + ChatColor.YELLOW + String.valueOf(LocalDateTime.now().until(coolDownDateTime, ChronoUnit.SECONDS)) + " Seconds" + ChatColor.RED + " before you can use a scroll of this type again!");
                return;
            }

            coolDownArray.remove(id);

            // Teleports Player if possible
            if (!teleportCommand.execute(id)) {
                Chat.chatError(player, "There was an error executing the scroll of teleportation.");
            }
        }
    }

    @EventHandler
    public static void preventScrollFromStacking(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        if (!(data.has(new NamespacedKey(Scrolls.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING))) return;
        if (event.isShiftClick()) event.setCancelled(true);

        data.set(new NamespacedKey(Scrolls.getPlugin(), "randomNumberToIdentify"), PersistentDataType.DOUBLE, Calcs.getRandomDouble());
        item.setItemMeta(meta);
    }

    @EventHandler
    public static void onLeftClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getItem() != null) {

                Player player = event.getPlayer();
                ItemStack item = event.getItem();
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();

                // Check if item is a scroll, otherwise returns
                if (!(data.has(new NamespacedKey(Scrolls.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING))) return;

                // Prevent Blocks from breaking
                event.setCancelled(true);

                Location loc = player.getTargetBlock(null, 100).getLocation();

                int[] cords = { loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ() };

                String destinationWorld = loc.getWorld().getName();

                data.set(new NamespacedKey(Scrolls.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY, cords);
                data.set(new NamespacedKey(Scrolls.getPlugin(), "destinationWorld"), PersistentDataType.STRING, destinationWorld);
                item.setItemMeta(meta);

                List<String> lore = meta.getLore();

                if (lore.size() >= 3) {
                    lore.remove(lore.size() - 1);
                    lore.remove(lore.size() - 1);
                }

                // Adding teleport location to scroll lore
                lore.add("");
                lore.add("§7X:" + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ() + " | World: " + WorldNameAdapter.cleanUp(destinationWorld));

                meta.setLore(lore);
                item.setItemMeta(meta);

                Chat.chatSuccess(player, "Spawn successfully written into the scroll!");
            }
        }
    }

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (event.getItem() != null) {
                ItemStack item = event.getItem();
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();

                // Check if item is a scroll, otherwise returns
                if (!(data.has(new NamespacedKey(Scrolls.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING))) return;

                UpgradeScroll gui = new UpgradeScroll();
                player.openInventory(gui.getInventory());
            }
        }
    }

    @EventHandler
    private void preventMovementWhileTeleporting(PlayerMoveEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        if (!TeleportCommand.animationStatusHas(id)) return;
        if (!event.getFrom().toVector().equals(event.getTo().toVector())) {
            event.setCancelled(true);
        }
    }
}
