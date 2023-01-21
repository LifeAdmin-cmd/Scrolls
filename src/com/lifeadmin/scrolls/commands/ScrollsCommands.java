package com.lifeadmin.scrolls.commands;

import com.lifeadmin.scrolls.events.ScrollEvents;
import com.lifeadmin.scrolls.factories.ScrollFactory;
import com.lifeadmin.scrolls.staticFunctions.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static java.lang.Integer.parseInt;

public class ScrollsCommands implements CommandExecutor {

    private final ScrollFactory scrollFactory = new ScrollFactory();

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
                    addTeleportScroll(1, player);
                } else if (args[0].equalsIgnoreCase("tp") && args.length == 2) {
                    if (parseInt(args[1]) > 3) { Chat.chatError(player,"This level doesn't exist!"); return true; }
                    addTeleportScroll(parseInt(args[1]), player);
                    Chat.chatSuccess(player, "Scroll Of Teleportation added to your inventory!");
                }  else if (args[0].equalsIgnoreCase("tp") && parseInt(args[1]) <= 3 && args.length == 3) {
                    Player target = getTargetPlayer(args[2]);
                    if (target == null) { Chat.chatError((Player) sender, "Player not found!"); return true; }
                    addTeleportScroll(parseInt(args[1]), target);
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

    private void addTeleportScroll(int level, Player player) {
        ItemStack scroll = scrollFactory.spawnTeleportScroll(level, player);
        player.getInventory().addItem(scroll);
    }

    private Player getTargetPlayer(String playerName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(playerName)) return player;
        }
        return null;
    }
}
