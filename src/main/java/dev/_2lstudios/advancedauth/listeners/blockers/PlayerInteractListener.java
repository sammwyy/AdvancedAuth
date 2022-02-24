package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerInteractListener extends BlockerListener {
    public PlayerInteractListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (!this.isAllowed(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
