package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;
import dev._2lstudios.advancedauth.bukkit.player.AuthPlayer;
import dev._2lstudios.advancedauth.bukkit.player.AuthPlayerManager;

public class BlockerListener implements Listener {
    protected final AuthPlayerManager players;
    protected final AdvancedAuth plugin;

    public BlockerListener(final AdvancedAuth plugin) {
        this.players = (AuthPlayerManager) plugin.getPluginPlayerManager();
        this.plugin = plugin;
    }

    public boolean isAllowed(final AuthPlayer player, final String action) {
        if (!player.getBukkitPlayer().isOnline()) {
            return true;
        }
        
        String blockMode = this.plugin.getMainConfig().getString("settings.actions.block", "default");

        if (blockMode.equalsIgnoreCase("never")) {
            return true;
        } else if (blockMode.equalsIgnoreCase("always")) {
            return false;
        } else {
            boolean isOptional = this.plugin.getMainConfig().getBoolean("authentication.is-register-optional", false);

            if (isOptional && !player.isRegistered()) {
                return true;
            } else {
                return player.isLogged();
            }
        }
    }

    public boolean isAllowed(final Player player, final String action) {
        final AuthPlayer authPlayer = (AuthPlayer) this.players.getPlayer(player);

        if (authPlayer == null && player.isOnline()) {
            return false;
        } else if (authPlayer == null) {
            return true;
        }

        return this.isAllowed(authPlayer, action);
    }
}