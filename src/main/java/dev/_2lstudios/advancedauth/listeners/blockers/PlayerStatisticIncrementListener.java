package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class PlayerStatisticIncrementListener extends BlockerListener {
    public PlayerStatisticIncrementListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent e) {
        if (!this.isAllowed((Player) e.getPlayer(), "deny-statistic-increment")) {
            e.setCancelled(true);
        }
    }
}