package com.lifeadmin.magic.events;

import com.lifeadmin.magic.Magic;
import com.lifeadmin.magic.items.ItemManager;
import com.lifeadmin.magic.staticFunctions.Calcs;
import com.lifeadmin.magic.staticFunctions.Chat;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ScrollEvents implements Listener {

    public ScrollEvents(Integer maxDistance, Integer coolDown, Boolean skipWorldCheck) {
        this.maxDistance = maxDistance;
        this.coolDown = coolDown;
        this.skipWorldCheck = skipWorldCheck;
    }

    private final double maxDistance;
    private final int coolDown;
    private final boolean skipWorldCheck;

    private final HashMap<UUID, LocalDateTime> coolDownArray = new HashMap<>();

    private final HashMap<UUID, Boolean> animationStatus = new HashMap<>();

    @EventHandler
    public void startTeleportEvent(PlayerToggleSneakEvent event) {
        if (!(event.getPlayer().isSneaking())) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item.getType() != Material.AIR) {
            Player player = event.getPlayer();
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
            if (!(data.has(new NamespacedKey(Magic.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY))) {
                if ((data.has(new NamespacedKey(Magic.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING))) {
                    Chat.chatError(player, "There was no destination set for this scroll!");
                    return;
                }
                return;
            }

            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                Chat.chatError(player, "You cannot cast this spell while moving!");
                return;
            }

            UUID id = player.getUniqueId();
            coolDownArray.putIfAbsent(id, LocalDateTime.now());
            LocalDateTime coolDownDateTime = coolDownArray.get(id);

            if (LocalDateTime.now().isBefore(coolDownDateTime)) {
                Chat.chatError(event.getPlayer(), "You have to wait: " + ChatColor.YELLOW + String.valueOf(LocalDateTime.now().until(coolDownDateTime, ChronoUnit.SECONDS)) + " Seconds" + ChatColor.RED + " before you can use a scroll of this type again!");
                return;
            }

            int[] cords = data.get(new NamespacedKey(Magic.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY);

            int x = cords[0];
            int y = cords[1];
            int z = cords[2];

            Location playerPos = player.getLocation();
            double distance = Math.sqrt(Math.pow(x - playerPos.getBlockX(), 2) + Math.pow(y - playerPos.getBlockY(), 2) + Math.pow(z - playerPos.getBlockZ(), 2));

            World destinationWorld = Bukkit.getWorld("world");
            if (player.getWorld() == destinationWorld || this.skipWorldCheck) {
                if (distance <= this.maxDistance || this.maxDistance == 0) {
                    Location loc = new Location(destinationWorld, x + 0.5, y, z + 0.5); // 0.5 places you in the middle of the Block
                    animationStatus.putIfAbsent(id, true);
                    player.getInventory().removeItem(item);
                    coolDownArray.put(id, LocalDateTime.now().plusSeconds(this.coolDown));
                    this.playTeleportAnimation(player, loc);
                } else {
                    Chat.chatError(player, "You are too far away from your Destination!");
                    Chat.chatWarning(player, "Your Destination: " + Arrays.toString(cords));
                    Chat.chatWarning(player, "Move " + Math.round(distance - maxDistance) + " blocks closer!");
                }
            } else {
                Chat.chatError(player, "You cannot teleport through worlds!");
                Chat.chatWarning(player, "Destination world: " + ChatColor.AQUA + destinationWorld.getName());
            }
        }
    }

    @EventHandler
    public static void preventScrollFromStacking(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        if (!(data.has(new NamespacedKey(Magic.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING))) return;
        if (event.isShiftClick()) event.setCancelled(true);

        data.set(new NamespacedKey(Magic.getPlugin(), "randomNumberToIdentify"), PersistentDataType.DOUBLE, Calcs.getRandomDouble());
        item.setItemMeta(meta);
    }

    @EventHandler
    public static void preventCommandScrollFromStacking(PlayerCommandPreprocessEvent event) {
        if (!(event.getMessage().equals("/basic_tp"))) return;

        ItemManager itemManager = Magic.getPlugin().getItemManager();
        ItemStack scroll = itemManager.getScrollOfTeleportation();
        ItemMeta meta = scroll.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(Magic.getPlugin(), "randomNumberToIdentify"), PersistentDataType.DOUBLE, Calcs.getRandomDouble());
        scroll.setItemMeta(meta);

        event.getPlayer().getInventory().addItem(scroll);
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
                if (!(data.has(new NamespacedKey(Magic.getPlugin(), "ScrollOfTeleportation"), PersistentDataType.STRING))) return;

                Location loc = player.getTargetBlock(null, 100).getLocation();

                int[] cords = { loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ() };

                data.set(new NamespacedKey(Magic.getPlugin(), "cords"), PersistentDataType.INTEGER_ARRAY, cords);
                item.setItemMeta(meta);
                Chat.chatSuccess(player, "Spawn successfully written into the scroll!");
            }
        }
    }

    @EventHandler
    private void preventMovementWhileTeleporting(PlayerMoveEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        if (!animationStatus.containsKey(id)) return;
        if (!event.getFrom().toVector().equals(event.getTo().toVector())) {
            event.setCancelled(true);
        }
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
        }, 0, (1000/repetitions));// wait 0 ms before doing the action and do it every repetitions per second
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
        }.runTask(Magic.getPlugin());
        Chat.chatSuccess(player, "Teleport successful");
    }
}
