package dev._2lstudios.advancedauth.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerLoginListener implements Listener {
    private AdvancedAuth plugin;
    
    public PlayerLoginListener(AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    public String getDefaultMessage(String key) {
        return ChatColor.translateAlternateColorCodes(
            '&',
            this.plugin.getLanguageManager().getLanguage(
                this.plugin.getLanguageManager().getDefaultLocale()
            ).getString(key));
    }

    @EventHandler
    public void onPlayerLogin (PlayerLoginEvent e) {
        // Country blocker.
        if (!this.plugin.getCountryCheck().canJoinAddress(e.getAddress().toString())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.getDefaultMessage("country-blocked"));
            return;
        }

        // Fail Lock.
        if (this.plugin.getFailLock().isAddressLocked(e.getAddress().toString())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.getDefaultMessage("faillock"));
            return;
        }
    }
}
