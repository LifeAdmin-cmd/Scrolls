package com.lifeadmin.scrolls.events;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.staticFunctions.Chat;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.*;

import static com.lifeadmin.scrolls.events.ScrollEvents.coolDownArray;

public class TeleportCommand {
    private static final HashMap<UUID, Boolean> animationStatus = new HashMap<>();

    private final ItemStack item;
    private final Player player;
    private PersistentDataContainer data;

    public static boolean animationStatusHas(UUID id) {
        return animationStatus.containsKey(id);
    }

    public TeleportCommand(ItemStack item, Player player) {
        this.item = item;
        this.player = player;
        data = item.getItemMeta().getPersistentDataContainer();
    }

    /**
     * Checks if player holding a scroll
     * @return true if scroll param is set in Item's meta data
     */
    public boolean isValidScroll() {
        return data.has(new NamespacedKey(Scrolls.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING);
    }

    /**
     * Checks if a scroll has valid cords
     * @return true if cords are saved in Items meta data
     */
    public boolean hasValidCords() {
        return data.has(new NamespacedKey(Scrolls.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY);
    }

    /**
     * Executing the teleportation
     * @return true if teleport was successful
     */
    public boolean execute(UUID id) {
        int[] cords = data.get(new NamespacedKey(Scrolls.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY);

        int x = cords[0];
        int y = cords[1];
        int z = cords[2];

        Location playerPos = player.getLocation();
        double distance = Math.sqrt(Math.pow(x - playerPos.getBlockX(), 2) + Math.pow(y - playerPos.getBlockY(), 2) + Math.pow(z - playerPos.getBlockZ(), 2));
        int maxDistance = data.get(new NamespacedKey(Scrolls.getPlugin(), "maxDistance"), PersistentDataType.INTEGER);
        int coolDown = data.get(new NamespacedKey(Scrolls.getPlugin(), "coolDown"), PersistentDataType.INTEGER);
        int skipWorldCheck = data.get(new NamespacedKey(Scrolls.getPlugin(), "skipWorldCheck"), PersistentDataType.INTEGER);
        String destinationWorld = data.get(new NamespacedKey(Scrolls.getPlugin(), "destinationWorld"), PersistentDataType.STRING);

        // World destinationWorld = Bukkit.getWorld("world");
        if (Objects.equals(player.getWorld().getName(), destinationWorld) || skipWorldCheck == 1) {
            if (distance <= maxDistance || maxDistance == 0 || !Objects.equals(player.getWorld().toString(), destinationWorld)) {
                Location loc = new Location(Bukkit.getWorld(destinationWorld), x + 0.5, y, z + 0.5); // 0.5 places you in the middle of the Block
                animationStatus.putIfAbsent(id, true);
                player.getInventory().removeItem(item);
                coolDownArray.put(id, LocalDateTime.now().plusSeconds(coolDown));
                this.playTeleportAnimation(player, loc);
            } else {
                Chat.chatError(player, "You are too far away from your Destination!");
                Chat.chatWarning(player, "Your Destination: " + Arrays.toString(cords));
                Chat.chatWarning(player, "Move " + Math.round(distance - maxDistance) + " blocks closer!");
            }
        } else {
            Chat.chatError(player, "You cannot teleport through dimensions with this scroll!");

            Chat.chatWarning(player, "Destination world: " + ChatColor.AQUA + WorldNameAdapter.cleanUp(destinationWorld));
        }
        return true;
    }

    private void playTeleportAnimation(Player player, Location loc) {
        UUID id = player.getUniqueId();
        animationStatus.put(id, true);

        Bukkit.getWorld("world").playSound(loc, Sound.BLOCK_PORTAL_TRIGGER, 8.0F, 0.5F);

        Timer timer = new Timer();
        int duration = 8;// duration in seconds
        int repetitions = 10;// how often the method is repeated every second
        timer.schedule(new TimerTask() {
            private int count = 0;
            private double circleHeight = 0;
            private double multiplier = 2 / ((double) repetitions * (double) duration);

            @Override
            public void run() {
                count++;
                circleHeight = circleHeight + multiplier;
                if (count >= (duration * repetitions)) {
                    timer.cancel();
                    teleportPlayer(player, loc);
                    return;
                }

                Bukkit.getWorld("world").spawnParticle(Particle.DOLPHIN, player.getEyeLocation().add(0, 20, 0), 200, 0, 20, 0);
                for (int degree = 0; degree < 360; degree++) {
                    double radians = Math.toRadians(degree);
                    double x = Math.cos(radians)*1.5;
                    double z = Math.sin(radians)*1.5;
                    Location loc = player.getLocation();
                    loc.add(x, circleHeight - 0.5, z);
                    player.getWorld().spawnParticle(Particle.PORTAL, loc, 0, 0, 0, 0);
                }
            }
        }, 0, (1000/repetitions));// wait 0 ms before doing the action and do it every x repetitions per second
    }

    private void teleportPlayer(Player player, Location loc) {
        UUID id = player.getUniqueId();
        animationStatus.remove(id);
        Bukkit.getWorld("world").playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 8.0F, 0.5F);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(loc);
            }
        }.runTask(Scrolls.getPlugin());
        Chat.chatSuccess(player, "Teleport successful");
    }
}
