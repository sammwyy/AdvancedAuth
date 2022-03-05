package dev._2lstudios.advancedauth.bukkit.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;

public class ProjectileLaunchListener extends BlockerListener {
    public ProjectileLaunchListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntity(), "deny-projectiles")) {
                e.setCancelled(true);
            }
        }
    }
}
