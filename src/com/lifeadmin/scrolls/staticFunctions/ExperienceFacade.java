package com.lifeadmin.scrolls.staticFunctions;

import org.bukkit.entity.Player;

public class ExperienceFacade {

    public static int getTotalExperience(Player player) {
        return Experience.getExp(player);
    }

    public int getExperienceFromLevel(int level) {
        return Experience.getExpFromLevel(level);
    }

    public double getLevelFromExperience(long exp) {
        return Experience.getLevelFromExp(exp);
    }

    public int getIntLevelFromExperience(long exp) {
        return Experience.getIntLevelFromExp(exp);
    }

    public static void changeExp(Player player, int exp) {
        Experience.changeExp(player, exp);
    }
}
