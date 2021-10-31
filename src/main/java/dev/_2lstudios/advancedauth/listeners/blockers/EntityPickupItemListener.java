package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class EntityPickupItemListener extends BlockerListener {
    public EntityPickupItemListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityPickupItem(final EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }
}