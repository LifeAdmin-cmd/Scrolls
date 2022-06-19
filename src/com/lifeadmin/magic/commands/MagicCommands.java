package com.lifeadmin.magic.commands;

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
            player.getInventory().addItem(ItemManager.scrollOfTeleportation);
            return true;
        }

        return true;
    }
}
