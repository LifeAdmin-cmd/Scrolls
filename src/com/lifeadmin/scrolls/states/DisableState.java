package com.lifeadmin.scrolls.states;

import com.lifeadmin.scrolls.Scrolls;
import com.lifeadmin.scrolls.events.ScrollEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DisableState implements State {
    @Override
    public void execute() {
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData() throws IOException {
        File file = new File(Scrolls.getPlugin().getDataFolder()+File.separator+"cooldowns.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            file.createNewFile(); //This needs a try catch
        }

        HashMap<UUID, LocalDateTime> cooldowns = ScrollEvents.coolDownArray;
        List<String> s = Scrolls.getPlugin().getConfig().getStringList("cooldowns");
        for (UUID id : cooldowns.keySet()) {
            s.add(id + ":" + cooldowns.get(id));
        }
        config.set("cooldowns", s);
        config.save(file);
    }
}
