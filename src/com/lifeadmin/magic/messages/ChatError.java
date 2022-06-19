package com.lifeadmin.magic.messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatError {
    public static void makeChatError(Player sender, String message) {
        sender.sendMessage(ChatColor.RED + message);
    }
}
