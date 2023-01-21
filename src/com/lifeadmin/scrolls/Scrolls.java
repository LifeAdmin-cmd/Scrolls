package com.lifeadmin.scrolls;

import com.lifeadmin.scrolls.commands.ScrollsCommands;
import com.lifeadmin.scrolls.events.InventoryEvents;
import com.lifeadmin.scrolls.events.ScrollEvents;
import com.lifeadmin.scrolls.items.ItemManager;
import com.lifeadmin.scrolls.states.DisableState;
import com.lifeadmin.scrolls.states.EnableState;
import com.lifeadmin.scrolls.states.State;
import org.bukkit.plugin.java.JavaPlugin;
public class Scrolls extends JavaPlugin {
    private static Scrolls plugin;

    public static Scrolls getPlugin() {
        return plugin;
    }

    private static ItemManager itemManager;

    public ItemManager getItemManager() {
        return itemManager;
    }

    private State state;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        itemManager = new ItemManager();
        ScrollsCommands commands = new ScrollsCommands();
        getCommand("scrolls").setExecutor(commands);
        getServer().getPluginManager().registerEvents(new ScrollEvents(), this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        state = new EnableState();
        state.execute();
    }

    @Override
    public void onDisable() {
        state = new DisableState();
        state.execute();
    }
}
