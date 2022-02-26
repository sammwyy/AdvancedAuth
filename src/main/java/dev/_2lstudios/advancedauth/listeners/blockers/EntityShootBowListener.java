package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class EntityShootBowListener extends BlockerListener {
    public EntityShootBowListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityShootBow(final EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntity(), "deny-interact")) {
                e.setCancelled(true);
            }
        }
    }
}
