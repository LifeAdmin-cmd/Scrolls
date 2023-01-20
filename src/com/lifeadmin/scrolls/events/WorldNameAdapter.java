package com.lifeadmin.scrolls.events;

import java.util.Objects;

public class WorldNameAdapter {
    /**
     * Changes the world name to look better to the player if
     * it is shown in a text or saved to a scroll
     */
    static String cleanUp(String worldName) {
        if (Objects.equals(worldName, "world")) {
            worldName = "Overworld";
            return worldName;
        }
        if (Objects.equals(worldName, "world_nether")) {
            worldName = "Nether";
            return worldName;
        }
        if (Objects.equals(worldName, "world_end")) {
            worldName = "The End";
            return worldName;
        }
        return worldName;
    }
}