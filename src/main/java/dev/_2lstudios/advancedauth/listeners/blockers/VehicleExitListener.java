package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleExitEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class VehicleExitListener extends BlockerListener {
    public VehicleExitListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onVehicleExit(final VehicleExitEvent e) {
        if (e.getExited() instanceof Player) {
            if (!this.isAllowed((Player) e.getExited(), "deny-vehicles")) {
                e.setCancelled(true);
            }
        }
    }
}
