package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class BlockPlaceListener extends BlockerListener {
    public BlockPlaceListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!this.isAllowed(e.getPlayer(), "deny-block-break-place")) {
            e.setCancelled(true);
        }
    }
}
