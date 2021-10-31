package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.advancedauth.player.AuthPlayerManager;

public class BlockerListener implements Listener {
    protected final AuthPlayerManager players;

    public BlockerListener(final AdvancedAuth plugin) {
        this.players = (AuthPlayerManager) plugin.getPluginPlayerManager();
    }

    public boolean isAllowed(final Player player) {
        final AuthPlayer authPlayer = (AuthPlayer) this.players.getPlayer(player);

        return authPlayer.isLogged();
    }
}