package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerMoveListener extends BlockerListener {
    public PlayerMoveListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!this.isAllowed(e.getPlayer(), "deny-move")) {
            if (e.getFrom().distance(e.getTo()) > 0) {
                e.setTo(e.getFrom());
            }
        }
    }
}
