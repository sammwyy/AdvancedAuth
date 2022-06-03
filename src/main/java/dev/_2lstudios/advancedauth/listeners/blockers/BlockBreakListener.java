package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class BlockBreakListener extends BlockerListener {
    public BlockBreakListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!isAllowed(e.getPlayer(), "deny-block-break-place")) {
            e.setCancelled(true);
        }
    }
}
