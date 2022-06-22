package com.lifeadmin.magic.commands;

import com.lifeadmin.magic.Magic;
import com.lifeadmin.magic.items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MagicCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("basic_tp")) {
            ItemManager itemManager = Magic.getPlugin().getItemManager();
            System.out.println("Manager im Command: " + itemManager);
            System.out.println("Managers return: " + itemManager.getScrollOfTeleportation());
            player.getInventory().addItem(itemManager.getScrollOfTeleportation());
            return true;
        }

        return true;
    }
}
