package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class BlockBreakListener extends BlockerListener {
    public BlockBreakListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        if (!isAllowed(e.getPlayer(), "deny-block-break-place")) {
            e.setCancelled(true);
        }
    }
}
