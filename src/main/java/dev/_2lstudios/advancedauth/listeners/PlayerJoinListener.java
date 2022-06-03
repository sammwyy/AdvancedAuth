package dev._2lstudios.advancedauth.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class PlayerJoinListener implements Listener {
    
    private AdvancedAuth plugin;

    public PlayerJoinListener (AdvancedAuth plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        AuthPlayer player = this.plugin.getPlayerManager().addPlayer(e.getPlayer());

        // Prevent movement
        player.updateEffects();

        // Hide other players
        for (AuthPlayer op : this.plugin.getPlayerManager().getPlayers()) {
            if (op.isHidden()) {
                player.hideOther(op.getBukkitPlayer());
            }
        }

        // Teleport spawn
        if (this.plugin.getConfig().getBoolean("settings.teleport-spawn.enabled")) {
            Location location = this.plugin.getConfig().getLocation("settings.teleport-spawn.location");
            e.getPlayer().teleport(location);
        }

        // Disable join message
        if (this.plugin.getConfig().getBoolean("settings.disable-join-quit-message")) {
            e.setJoinMessage(null);
        }
    }
}
