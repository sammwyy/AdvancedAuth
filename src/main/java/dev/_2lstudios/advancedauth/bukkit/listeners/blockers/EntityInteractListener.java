package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityInteractEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class EntityInteractListener extends BlockerListener {
    public EntityInteractListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityInteract(final EntityInteractEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntity(), "deny-interact")) {
                e.setCancelled(true);
            }
        }
    }
}
