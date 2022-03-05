package dev._2lstudios.advancedauth.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class PlayerQuitListener implements Listener {
    
    private final AdvancedAuth plugin;

    public PlayerQuitListener (final AdvancedAuth plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit (final PlayerQuitEvent e) {
        // Disable join message
        if (this.plugin.getMainConfig().getBoolean("settings.disable-join-quit-message")) {
            e.setQuitMessage(null);
        }
    }
}
