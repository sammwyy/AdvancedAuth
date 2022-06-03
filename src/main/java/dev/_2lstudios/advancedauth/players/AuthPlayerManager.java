package dev._2lstudios.advancedauth.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class AuthPlayerManager {
    private AdvancedAuth plugin;

    private Map<Player, AuthPlayer> players;

    public AuthPlayerManager(AdvancedAuth plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    public AuthPlayer addPlayer(Player bukkitPlayer) {
        AuthPlayer player = new AuthPlayer(this.plugin, bukkitPlayer);
        this.players.put(bukkitPlayer, player);
        return player;
    }

    public AuthPlayer removePlayer(Player bukkitPlayer) {
        return this.players.remove(bukkitPlayer);
    }

    public AuthPlayer getPlayer(Player bukkitPlayer) {
        return this.players.get(bukkitPlayer);
    }

    public AuthPlayer getPlayer(String name) {
        Player bukkitPlayer = this.plugin.getServer().getPlayerExact(name);
        if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
            return this.getPlayer(bukkitPlayer);
        } else {
            return null;
        }
    }

    public Collection<AuthPlayer> getPlayers() {
        return this.players.values();
    }

    public void clear() {
        this.players.clear();
    }

    public void addAll() {
        for (Player bukkitPlayer : this.plugin.getServer().getOnlinePlayers()) {
            this.addPlayer(bukkitPlayer);
        }
    }
}