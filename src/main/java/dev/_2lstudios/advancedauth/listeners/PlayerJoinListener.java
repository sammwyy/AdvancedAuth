package dev._2lstudios.advancedauth.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerJoinListener implements Listener {
    
    private final AdvancedAuth plugin;

    public PlayerJoinListener (final AdvancedAuth plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin (final PlayerJoinEvent e) {
        e.getPlayer().setWalkSpeed(0.0f);
        
        if (this.plugin.getMainConfig().getBoolean("settings.teleport-spawn.enabled")) {
            final Location location = this.plugin.getMainConfig().getLocation("settings.teleport-spawn.location", true);
            e.getPlayer().teleport(location);
        }
    }
}
