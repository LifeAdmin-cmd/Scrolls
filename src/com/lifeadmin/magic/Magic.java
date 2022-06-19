package com.lifeadmin.magic;

import com.lifeadmin.magic.commands.MagicCommands;
import com.lifeadmin.magic.events.ScrollEvents;
import com.lifeadmin.magic.items.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Magic extends JavaPlugin {

    @Override
    public void onEnable() {
        ItemManager.init();
        MagicCommands commands = new MagicCommands();
        getCommand("basic_tp").setExecutor(commands);
        getServer().getPluginManager().registerEvents(new ScrollEvents(), this);
    }

    @Override
    public void onDisable() {

    }

}
