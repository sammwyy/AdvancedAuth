package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class VehicleEnterListener extends BlockerListener {
    public VehicleEnterListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent e) {
        if (e.getEntered() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntered(), "deny-vehicles")) {
                e.setCancelled(true);
            }
        }
    }
}
