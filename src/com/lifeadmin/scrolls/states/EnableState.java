package com.lifeadmin.scrolls.states;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.events.ScrollEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EnableState implements State {
    @Override
    public void execute() {
        try {
            retrieveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void retrieveData() throws IOException {
        File file = new File(Scrolls.getPlugin().getDataFolder(), "cooldowns.yml");
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
}
