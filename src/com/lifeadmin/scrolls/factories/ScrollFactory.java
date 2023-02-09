package com.lifeadmin.scrolls.factories;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ScrollFactory {
    public ScrollFactory() {
    }
    public ItemStack spawnTeleportScroll(int level) {
        ItemStack scroll;

        if (level == 2) {
            scroll = new ScrollLevel2Factory().createScroll();
        } else if (level == 3) {
            scroll = new ScrollLevel3Factory().createScroll();
        } else {
            scroll = new ScrollLevel1Factory().createScroll();
        }

        return scroll;
    }
}