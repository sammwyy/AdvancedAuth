package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class ProjectileLaunchListener extends BlockerListener {
    public ProjectileLaunchListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntity(), "deny-projectiles")) {
                e.setCancelled(true);
            }
        }
    }
}
