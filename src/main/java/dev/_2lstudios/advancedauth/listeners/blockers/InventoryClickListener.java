package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class InventoryClickListener extends BlockerListener {
    public InventoryClickListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (!this.isAllowed((Player) e.getWhoClicked())) {
                e.setCancelled(true);
            }
        }
    }
}
