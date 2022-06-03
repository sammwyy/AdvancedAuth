package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class BlockerListener implements Listener {
    private AdvancedAuth plugin;

    public BlockerListener(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    public boolean isAllowed(final AuthPlayer player, final String action) {
        if (!player.getBukkitPlayer().isOnline()) {
            return true;
        }
        
        String blockMode = this.plugin.getConfig().getString("settings.actions.block", "default");

        if (blockMode.equalsIgnoreCase("never")) {
            return true;
        } else if (blockMode.equalsIgnoreCase("always")) {
            return false;
        } else {
            boolean isOptional = this.plugin.getConfig().getBoolean("authentication.is-register-optional", false);

            if (isOptional && !player.isRegistered()) {
                return true;
            } else {
                return player.isLogged();
            }
        }
    }

    public boolean isAllowed(final Player player, final String action) {
        final AuthPlayer authPlayer = this.plugin.getPlayerManager().getPlayer(player);

        if (authPlayer == null && player.isOnline()) {
            return false;
        } else if (authPlayer == null) {
            return true;
        }

        return this.isAllowed(authPlayer, action);
    }
}