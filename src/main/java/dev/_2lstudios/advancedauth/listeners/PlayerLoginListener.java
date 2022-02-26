package dev._2lstudios.advancedauth.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerLoginListener implements Listener {
    private final AdvancedAuth plugin;
    
    public PlayerLoginListener(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin (final PlayerLoginEvent e) {
        if (!this.plugin.getCountryCheck().canJoinAddress(e.getRealAddress().toString())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your country has been blocked");
        }
    }
}
