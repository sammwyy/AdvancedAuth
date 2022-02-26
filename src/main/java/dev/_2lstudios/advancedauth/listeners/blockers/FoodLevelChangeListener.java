package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class FoodLevelChangeListener extends BlockerListener {
    public FoodLevelChangeListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.isAllowed((Player) e.getEntity(), "deny-food-level-change")) {
                e.setCancelled(true);
            }
        }
    }
}
