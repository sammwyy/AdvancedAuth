package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class AsyncPlayerChatListener extends BlockerListener {
    public AsyncPlayerChatListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!isAllowed(player, "deny-chat")) {
            e.setCancelled(true);
        }
    }
}
