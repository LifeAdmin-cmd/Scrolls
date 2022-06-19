package com.lifeadmin.magic.events;

import com.lifeadmin.magic.items.ItemManager;
import com.lifeadmin.magic.messages.ChatError;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ScrollEvents implements Listener {

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (event.getItem().getItemMeta().equals(ItemManager.scrollOfTeleportation.getItemMeta())) {
                    Player player = event.getPlayer();
//                    player.getWorld().createExplosion(player.getLocation(), 2.0f);
                    int x = player.getLocation().getBlockX() + 5;
                    int y = player.getLocation().getBlockY() + 1;
                    int z = player.getLocation().getBlockZ() + 5;
                    Location loc = new Location(player.getWorld(), x, y, z);
                    player.teleport(loc);
                    ChatError.makeChatError(player, "Teleport successful");
                }
            }
        }
    }
}
