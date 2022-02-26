package dev._2lstudios.advancedauth.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.jelly.player.IPluginPlayerManager;

public class AuthPlayerManager implements IPluginPlayerManager {

    private final AdvancedAuth plugin;
    private final Map<Player, AuthPlayer> players;

    public AuthPlayerManager(final AdvancedAuth plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    @Override
    public AuthPlayer addPlayer(final Player bukkitPlayer) {
        return this.players.put(bukkitPlayer, new AuthPlayer(this.plugin, bukkitPlayer));
    }

    @Override
    public void clear() {
        this.players.clear();
    }

    @Override
    public AuthPlayer getPlayer(final Player bukkitPlayer) {
        return this.players.get(bukkitPlayer);
    }

    @Override
    public AuthPlayer removePlayer(final Player bukkitPlayer) {
        return this.players.remove(bukkitPlayer);
    }

}
