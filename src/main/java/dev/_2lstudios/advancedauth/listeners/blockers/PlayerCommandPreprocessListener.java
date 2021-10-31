package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerCommandPreprocessListener extends BlockerListener {
    public PlayerCommandPreprocessListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent e) {
        final Player player = e.getPlayer();

        if (!isAllowed(player)) {
            e.setCancelled(true);
        }
    }
}
