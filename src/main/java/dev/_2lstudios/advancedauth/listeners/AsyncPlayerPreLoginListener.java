package dev._2lstudios.advancedauth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class AsyncPlayerPreLoginListener implements Listener {
    private AdvancedAuth plugin;
    
    public AsyncPlayerPreLoginListener(AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin (AsyncPlayerPreLoginEvent e) {
        if (this.plugin.getConfig().getBoolean("settings.prevent-logged-from-another-location")) {
            for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(e.getName())) {
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Already connected to this server");
                }
            }
        }
    }
}
