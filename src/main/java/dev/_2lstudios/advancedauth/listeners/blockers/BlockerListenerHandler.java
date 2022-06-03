package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.plugin.PluginManager;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class BlockerListenerHandler {
    public static void register(AdvancedAuth plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new AsyncPlayerChatListener(plugin), plugin);
        pm.registerEvents(new BlockBreakListener(plugin), plugin);
        pm.registerEvents(new BlockPlaceListener(plugin), plugin);
        pm.registerEvents(new EntityDamageByEntityListener(plugin), plugin);
        pm.registerEvents(new EntityDamageListener(plugin), plugin);
        pm.registerEvents(new EntityInteractListener(plugin), plugin);
        pm.registerEvents(new EntityPickupItemListener(plugin), plugin);
        pm.registerEvents(new EntityRegainHealthListener(plugin), plugin);
        pm.registerEvents(new EntityShootBowListener(plugin), plugin);
        pm.registerEvents(new EntityTargetListener(plugin), plugin);
        pm.registerEvents(new FoodLevelChangeListener(plugin), plugin);
        pm.registerEvents(new InventoryClickListener(plugin), plugin);
        pm.registerEvents(new InventoryCreativeListener(plugin), plugin);
        pm.registerEvents(new InventoryDragListener(plugin), plugin);
        pm.registerEvents(new InventoryInteractListener(plugin), plugin);
        pm.registerEvents(new InventoryOpenListener(plugin), plugin);
        pm.registerEvents(new PlayerCommandPreprocessListener(plugin), plugin);
        pm.registerEvents(new PlayerInteractListener(plugin), plugin);
        pm.registerEvents(new PlayerMoveListener(plugin), plugin);
        pm.registerEvents(new PlayerStatisticIncrementListener(plugin), plugin);
        pm.registerEvents(new ProjectileLaunchListener(plugin), plugin);
        pm.registerEvents(new VehicleEnterListener(plugin), plugin);
        pm.registerEvents(new VehicleExitListener(plugin), plugin);
    }
}