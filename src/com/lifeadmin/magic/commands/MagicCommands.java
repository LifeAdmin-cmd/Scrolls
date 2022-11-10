package com.lifeadmin.magic.commands;

import com.lifeadmin.magic.Magic;
import com.lifeadmin.magic.inventories.UpgradeScroll;
import com.lifeadmin.magic.items.ItemManager;
import com.lifeadmin.magic.staticFunctions.Chat;
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
            Chat.chatSuccess(player, "Scroll Of Teleportation added to your inventory!");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("confirm")) {
            UpgradeScroll gui = new UpgradeScroll();
            player.openInventory(gui.getInventory());
            return true;
        }

        return true;
    }
}
