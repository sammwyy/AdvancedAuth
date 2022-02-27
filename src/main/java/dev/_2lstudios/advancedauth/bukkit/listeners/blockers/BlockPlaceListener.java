package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class BlockPlaceListener extends BlockerListener {
    public BlockPlaceListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        if (!this.isAllowed(e.getPlayer(), "deny-block-break-place")) {
            e.setCancelled(true);
        }
    }
}
