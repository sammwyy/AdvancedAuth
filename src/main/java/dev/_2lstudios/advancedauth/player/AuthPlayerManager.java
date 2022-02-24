package dev._2lstudios.advancedauth.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.jelly.player.IPluginPlayerManager;
import dev._2lstudios.jelly.player.PluginPlayer;

public class AuthPlayerManager implements IPluginPlayerManager {

    private final AdvancedAuth plugin;
    private final Map<Player, AuthPlayer> players;

    public AuthPlayerManager(final AdvancedAuth plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    @Override
    public PluginPlayer addPlayer(final Player bukkitPlayer) {
        return this.players.put(bukkitPlayer, new AuthPlayer(this.plugin, bukkitPlayer));
    }

    @Override
    public void clear() {
        this.players.clear();
    }

    @Override
    public PluginPlayer getPlayer(final Player bukkitPlayer) {
        return this.players.get(bukkitPlayer);
    }

    @Override
    public PluginPlayer removePlayer(final Player bukkitPlayer) {
        return this.players.remove(bukkitPlayer);
    }

}
