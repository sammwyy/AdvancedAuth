package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class EntityRegainHealthListener extends BlockerListener {
    public EntityRegainHealthListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityRegainHealthEvent(final EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntity(), "deny-health-change")) {
                e.setAmount(0);
                e.setCancelled(true);
            }
        }
    }
}
