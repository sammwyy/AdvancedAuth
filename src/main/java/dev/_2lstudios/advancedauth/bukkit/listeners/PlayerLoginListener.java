package dev._2lstudios.advancedauth.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class PlayerLoginListener implements Listener {
    private final AdvancedAuth plugin;
    
    public PlayerLoginListener(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin (final PlayerLoginEvent e) {
        // Country blocker.
        if (!this.plugin.getCountryCheck().canJoinAddress(e.getAddress().toString())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your country has been blocked");
            return;
        }

        // Fail Lock.
        if (this.plugin.getFailLock().isAddressLocked(e.getAddress().toString())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Fail locked");
            return;
        }
    }
}
