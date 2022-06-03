package dev._2lstudios.advancedauth.listeners;

import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerLoginListener implements Listener {
    private AdvancedAuth plugin;
    private Pattern nameRegex;
    
    public PlayerLoginListener(AdvancedAuth plugin) {
        this.plugin = plugin;

        if (this.plugin.getConfig().getBoolean("security.block-invalid-usernames.enabled")) {
            this.nameRegex = Pattern.compile(plugin.getConfig().getString("security.block-invalid-usernames.regex"));
        }
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
        // Username checkers.
        if (this.plugin.getConfig().getBoolean("security.block-invalid-usernames.enabled")) {
            if (!this.nameRegex.matcher(e.getPlayer().getName()).matches()) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.getDefaultMessage("invalid-username"));
                return;
            }
        }

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
