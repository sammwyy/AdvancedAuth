package dev._2lstudios.advancedauth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerLoginListener implements Listener {
    private final AdvancedAuth plugin;
    
    public PlayerLoginListener(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin (final AsyncPlayerPreLoginEvent e) {
        System.out.println(e.getName());
        if (this.plugin.getMainConfig().getBoolean("settings.prevent-logged-from-another-location")) {
            for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(e.getName())) {
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Already connected to this server");
                }
            }
        }
    }
}
