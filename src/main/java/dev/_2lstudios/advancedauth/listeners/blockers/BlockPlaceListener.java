package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class BlockPlaceListener extends BlockerListener {
    public BlockPlaceListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        if (!this.isAllowed(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
