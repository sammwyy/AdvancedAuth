package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class EntityDamageByEntityListener extends BlockerListener {
    public EntityDamageByEntityListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Player)) {
            if (!this.isAllowed((Player) e.getDamager())) {
                e.setDamage(0);
                e.getEntity().setFireTicks(0);
                e.setCancelled(true);
            }
        }
    }
}
