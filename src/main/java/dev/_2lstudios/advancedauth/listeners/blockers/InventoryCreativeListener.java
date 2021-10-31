package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCreativeEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class InventoryCreativeListener extends BlockerListener {
    public InventoryCreativeListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryCreative(final InventoryCreativeEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (!this.isAllowed((Player) e.getWhoClicked())) {
                e.setCancelled(true);
            }
        }
    }
}
