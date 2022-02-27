package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class InventoryClickListener extends BlockerListener {
    public InventoryClickListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (!this.isAllowed((Player) e.getWhoClicked(), "deny-inventory")) {
                e.setCancelled(true);
            }
        }
    }
}
