package com.lifeadmin.scrolls;

import com.lifeadmin.scrolls.commands.ScrollsCommands;
import com.lifeadmin.scrolls.events.InventoryEvents;
import com.lifeadmin.scrolls.events.ScrollEvents;
import com.lifeadmin.scrolls.items.ItemManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Scrolls extends JavaPlugin {
    private static Scrolls plugin;

    public static Scrolls getPlugin() {
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
        ScrollsCommands commands = new ScrollsCommands();
        getCommand("scrolls").setExecutor(commands);
        getServer().getPluginManager().registerEvents(new ScrollEvents(), this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        try {
            retrieveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void retrieveData() throws IOException {
        File file = new File(getDataFolder(), "cooldowns.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        HashMap<UUID, LocalDateTime> cooldowns = ScrollEvents.coolDownArray;
        List<String> s = config.getStringList("cooldowns");
        for (String str : s) {
            String[] words = str.split(":", 2);
            System.out.println(words[0]);
            cooldowns.put(UUID.fromString(words[0]), LocalDateTime.parse(words[1], DateTimeFormatter.ISO_DATE_TIME));
        }
        ScrollEvents.coolDownArray = cooldowns;
        config.set("cooldowns", new ArrayList<>());
        config.save(file);
    }

    private void saveData() throws IOException {
        File file = new File(plugin.getDataFolder()+File.separator+"cooldowns.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            file.createNewFile(); //This needs a try catch
        }

        HashMap<UUID, LocalDateTime> cooldowns = ScrollEvents.coolDownArray;
        List<String> s = getConfig().getStringList("cooldowns");
        for (UUID id : cooldowns.keySet()) {
            s.add(id + ":" + cooldowns.get(id));
        }
        config.set("cooldowns", s);
        config.save(file);
    }
}
