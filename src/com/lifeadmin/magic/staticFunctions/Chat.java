package com.lifeadmin.magic.staticFunctions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {
    private static final String prefix = ChatColor.LIGHT_PURPLE + "[Magic] ";

    public static void chatError(Player sender, String message) {
        sender.sendMessage(prefix + ChatColor.RED + message);
    }

    public static void chatSuccess(Player sender, String message) {
        sender.sendMessage(prefix + ChatColor.GREEN + message);
    }

    public static void chatWarning(Player sender, String message) {
        sender.sendMessage(prefix + ChatColor.YELLOW + message);
    }
}
