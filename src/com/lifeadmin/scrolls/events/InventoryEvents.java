package com.lifeadmin.scrolls.events;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.inventories.UpgradeScroll;
import com.lifeadmin.scrolls.staticFunctions.Chat;
import jdk.nashorn.internal.objects.NativeFloat32Array;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {

    private final ScrollFacade scrollFacade = new ScrollFacade(this);

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() instanceof UpgradeScroll) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                scrollFacade.upgradeScroll(player, player.getInventory().getItemInMainHand());
            }
            else if (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                Chat.chatError(player, "Upgrade process cancelled");
                player.closeInventory();
            }
        }
    }
}
