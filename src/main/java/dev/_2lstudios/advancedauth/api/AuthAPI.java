package dev._2lstudios.advancedauth.api;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class AuthAPI {
    private static AdvancedAuth plugin;
    
    public AuthAPI(AdvancedAuth plugin) {
        AuthAPI.plugin = plugin;
    }

    public static AuthPlayer getPlayer(Player player) {
        return plugin.getPlayerManager().getPlayer(player);
    }
}
