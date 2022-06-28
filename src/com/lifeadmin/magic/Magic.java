package com.lifeadmin.magic;

import com.lifeadmin.magic.commands.MagicCommands;
import com.lifeadmin.magic.events.ScrollEvents;
import com.lifeadmin.magic.items.ItemManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Magic extends JavaPlugin {
    private static Magic plugin;

    public static Magic getPlugin() {
        return plugin;
    }

    private static ItemManager itemManager;

    public ItemManager getItemManager() {
        return itemManager;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        itemManager = new ItemManager();
        MagicCommands commands = new MagicCommands();
        getCommand("basic_tp").setExecutor(commands);
        getServer().getPluginManager().registerEvents(new ScrollEvents(getConfig().getInt("maxDistance"), getConfig().getInt("coolDown"), getConfig().getBoolean("skipWorldCheck")), this);
    }

    @Override
    public void onDisable() {

    }

}
