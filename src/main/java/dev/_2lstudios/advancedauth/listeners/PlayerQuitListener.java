package dev._2lstudios.advancedauth.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerQuitListener implements Listener {
    
    private AdvancedAuth plugin;

    public PlayerQuitListener (AdvancedAuth plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent e) {
        // Disable join message
        if (this.plugin.getConfig().getBoolean("settings.disable-join-quit-message")) {
            e.setQuitMessage(null);
        }
    }
}
