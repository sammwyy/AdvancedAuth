package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class AsyncPlayerChatListener extends BlockerListener {
    public AsyncPlayerChatListener(final AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();

        if (!isAllowed(player)) {
            e.setCancelled(true);
        }
    }
}
