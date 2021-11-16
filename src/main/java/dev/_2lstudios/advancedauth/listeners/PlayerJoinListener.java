package dev._2lstudios.advancedauth.listeners;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerJoinListener {
    
    private final AdvancedAuth plugin;

    public PlayerJoinListener (final AdvancedAuth plugin) {
        this.plugin = plugin;
    }
    
    public void onPlayerJoin (final PlayerJoinEvent e) {
        if (this.plugin.getMainConfig().getBoolean("settings.teleport-spawn.enabled")) {
            final Location location = this.plugin.getMainConfig().getLocation("settings.teleport-spawn.location", true);
            e.getPlayer().teleport(location);
        }
    }
}
