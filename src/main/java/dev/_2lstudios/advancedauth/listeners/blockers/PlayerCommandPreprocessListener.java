package dev._2lstudios.advancedauth.listeners.blockers;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class PlayerCommandPreprocessListener extends BlockerListener {
    private AdvancedAuth plugin;

    public PlayerCommandPreprocessListener(AdvancedAuth plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        AuthPlayer player = plugin.getPlayerManager().getPlayer(e.getPlayer());

        String command = e.getMessage().split(" ")[0];
        List<String> allowedCommands = this.plugin.getConfig().getStringList("security.allowed-commands");

        if (!allowedCommands.contains(command) && !isAllowed(player, "deny-commands")) {
            player.sendI18nMessage("login.not-logged");
            e.setCancelled(true);
        }
    }
}
