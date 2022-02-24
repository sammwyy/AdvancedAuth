package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.advancedauth.player.AuthPlayerManager;

public class BlockerListener implements Listener {
    protected final AuthPlayerManager players;
    protected final AdvancedAuth plugin;

    public BlockerListener(final AdvancedAuth plugin) {
        this.players = (AuthPlayerManager) plugin.getPluginPlayerManager();
        this.plugin = plugin;
    }

    public boolean isAllowed(final AuthPlayer player) {
        if (!player.getBukkitPlayer().isOnline()) {
            return true;
        }

        return player.isLogged();
    }

    public boolean isAllowed(final Player player) {
        final AuthPlayer authPlayer = (AuthPlayer) this.players.getPlayer(player);

        if (authPlayer == null && player.isOnline()) {
            return false;
        } else if (authPlayer == null) {
            return true;
        }

        return this.isAllowed(authPlayer);
    }
}