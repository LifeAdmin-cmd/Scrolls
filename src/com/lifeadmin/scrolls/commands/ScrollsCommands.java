package com.lifeadmin.scrolls.commands;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.events.ScrollEvents;
import com.lifeadmin.scrolls.items.ItemManager;
import com.lifeadmin.scrolls.staticFunctions.Calcs;
import com.lifeadmin.scrolls.staticFunctions.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static java.lang.Integer.parseInt;

public class ScrollsCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("scrolls")) {
            if (args.length == 0) {
                helpMessage(player);
            } else {
                if (args[0].equalsIgnoreCase("help")) {
                    helpMessage(player);
                    return true;
                } else if (args[0].equalsIgnoreCase("tp") && args.length == 1) {
                    spawnTeleportScroll(1, player);
                } else if (args[0].equalsIgnoreCase("tp") && args.length == 2) {
                    if (parseInt(args[1]) > 3) { Chat.chatError(player,"This level doesn't exist!"); return true; }
                    spawnTeleportScroll(parseInt(args[1]), player);
                    Chat.chatSuccess(player, "Scroll Of Teleportation added to your inventory!");
                }  else if (args[0].equalsIgnoreCase("tp") && parseInt(args[1]) <= 3 && args.length == 3) {
                    Player target = getTargetPlayer(args[2]);
                    if (target == null) { Chat.chatError((Player) sender, "Player not found!"); return true; }
                    spawnTeleportScroll(parseInt(args[1]), target);
                    Chat.chatSuccess(player, "Scroll Of Teleportation added to " + ChatColor.AQUA + target.getName() + ChatColor.GREEN + " inventory!");
                } else if (args[0].equalsIgnoreCase("resetCd") && args.length == 1) {
                    return false;
                } else if (args[0].equalsIgnoreCase("resetCd") && args.length == 2 && args[1].equalsIgnoreCase("all")) {
                    ScrollEvents.coolDownArray = new HashMap<>();
                    Chat.chatSuccess((Player) sender, "Resetted all cooldowns");
                    return true;
                } else if (args[0].equalsIgnoreCase("resetCd") && args.length == 2) {
                    Player target = getTargetPlayer(args[1]);
                    if (target == null) {
                        Chat.chatError((Player) sender, "Player not found!");
                        return true;
                    }
                    ScrollEvents.coolDownArray.remove(target.getUniqueId());
                    Chat.chatSuccess((Player) sender, "Resetted " + ChatColor.AQUA + target.getName() + ChatColor.GREEN + " cooldown");
                } else {
                    Chat.chatError((Player) sender, "Command not found!");
                    helpMessage((player));
                }
            }
            return true;
        }
        return true;
    }

    private void helpMessage(Player player) {
        player.sendMessage(ChatColor.YELLOW + "----------------------");
        player.sendMessage(ChatColor.YELLOW + "/scrolls tp <level> <player> - Gives a teleport scroll to a player");
        player.sendMessage(ChatColor.YELLOW + "/scrolls resetCd <player> - Reset a players scroll cooldown");
        player.sendMessage(ChatColor.YELLOW + "----------------------");
    }

    private void spawnTeleportScroll(int level, Player player) {
        ItemManager itemManager = Scrolls.getPlugin().getItemManager();
        ItemStack scroll = itemManager.scrollOfTeleportation;
        ItemMeta meta = scroll.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(Scrolls.getPlugin(), "randomNumberToIdentify"), PersistentDataType.DOUBLE, Calcs.getRandomDouble());

        if (level == 2) {
            meta.setDisplayName("§9Greater Scroll Of Teleportation");
            List<String> lore = meta.getLore();
            lore.set(0, "§7This Scroll Of Teleportation was made");
            lore.set(1, "§7with excellent expertise and craftsmanship");
            meta.setLore(lore);
        } else if (level == 3) {
            meta.setDisplayName("§6Ultimate Scroll Of Teleportation");
            List<String> lore = meta.getLore();
            lore.set(0, "§7This perfect Scroll Of Teleportation was made");
            lore.set(1, "§7with superlative expertise and craftsmanship");
            meta.setLore(lore);
        }

        if (level > 1) {
            FileConfiguration config = Scrolls.getPlugin().getConfig();
            data.set(new NamespacedKey(Scrolls.getPlugin(), "scrollLevel"), PersistentDataType.INTEGER, level);
            data.set(new NamespacedKey(Scrolls.getPlugin(), "maxDistance"), PersistentDataType.INTEGER, config.getInt("maxDistance_lvl" + level));
            data.set(new NamespacedKey(Scrolls.getPlugin(), "coolDown"), PersistentDataType.INTEGER, config.getInt("coolDown_lvl" + level));
            data.set(new NamespacedKey(Scrolls.getPlugin(), "skipWorldCheck"), PersistentDataType.INTEGER, config.getInt("skipWorldCheck_lvl" + level));
        }
        scroll.setItemMeta(meta);

        player.getInventory().addItem(scroll);
    }

    private Player getTargetPlayer(String playerName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(playerName)) return player;
        }
        return null;
    }
}
