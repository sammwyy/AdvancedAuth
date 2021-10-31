package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class EntityDamageListener extends BlockerListener {
    public EntityDamageListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        if (!this.isAllowed((Player) e.getEntity())) {
            e.setCancelled(true);
        }
    }
}
