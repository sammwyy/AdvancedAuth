package dev._2lstudios.advancedauth.bukkit.tasks;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;
import dev._2lstudios.advancedauth.bukkit.player.AuthPlayer;

public class PlayerDataFetchTask implements Runnable {

    private final AdvancedAuth plugin;

    public PlayerDataFetchTask(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            final AuthPlayer authPlayer = (AuthPlayer) this.plugin.getPluginPlayerManager().getPlayer(player);

            if (authPlayer != null && !authPlayer.isFetched()) {
                if (authPlayer.getLocale() != null) {
                    authPlayer.fetchUserData();
                }
            }
        }
    }

}
