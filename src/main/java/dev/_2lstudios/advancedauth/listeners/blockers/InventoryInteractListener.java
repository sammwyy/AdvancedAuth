package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryInteractEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class InventoryInteractListener extends BlockerListener {
    public InventoryInteractListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (!this.isAllowed((Player) e.getWhoClicked(), "deny-inventory")) {
                e.setCancelled(true);
            }
        }
    }
}