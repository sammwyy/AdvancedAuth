package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class PlayerMoveListener extends BlockerListener {
    public PlayerMoveListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) {
        if (!this.isAllowed(e.getPlayer(), "deny-move")) {
            if (e.getFrom().distance(e.getTo()) > 0) {
                e.setTo(e.getFrom());
            }
        }
    }
}
