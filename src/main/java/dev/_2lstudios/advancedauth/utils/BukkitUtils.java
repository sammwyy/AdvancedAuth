package dev._2lstudios.advancedauth.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;

public class BukkitUtils {
    public static Sound getSound(String name) {
        for (Sound sound : Sound.values()) {
            if (name.equalsIgnoreCase(sound.name())) {
                return sound;
            }
        }

        Bukkit.getLogger().warning("Couldn't load sound '" + name + "' (Invalid name?)");
        return null;
    }

    public static Material getMaterial(String name) {
        for (Material mat : Material.values()) {
            if (name.equals(mat.name())) {
                return mat;
            }
        }

        Bukkit.getLogger().warning("Couldn't load material '" + name + "' (Invalid name?)");
        return null;
    }
}
