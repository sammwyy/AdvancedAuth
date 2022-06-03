package dev._2lstudios.advancedauth.players;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class OfflinePlayer extends AuthPlayer {
    private String username;

    public OfflinePlayer(AdvancedAuth plugin, Player bukkitPlayer, String username) {
        super(plugin, bukkitPlayer);
        this.username = username.toLowerCase();
    }

    @Override
    public String getLowerName() {
        return this.username;
    }
}