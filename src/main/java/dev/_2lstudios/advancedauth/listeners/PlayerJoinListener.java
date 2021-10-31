package dev._2lstudios.advancedauth.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerJoinListener implements Listener {

    // private final AdvancedAuth plugin;

    public PlayerJoinListener(final AdvancedAuth plugin) {
        // this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent e) {

    }
}
