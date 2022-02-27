package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class EntityPickupItemListener extends BlockerListener {
    public EntityPickupItemListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityPickupItem(final EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntity(), "deny-item-pickup")) {
                e.setCancelled(true);
            }
        }
    }
}