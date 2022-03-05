package dev._2lstudios.advancedauth.tasks;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayer;

public class PlayerTimeoutTask implements Runnable {

    private final AdvancedAuth plugin;
    private final long timeout;

    public PlayerTimeoutTask(final AdvancedAuth plugin) {
        this.plugin = plugin;
        this.timeout = this.plugin.getConfig().getInt("authentication.timeout", 30);
    }

    @Override
    public void run() {
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            final AuthPlayer authPlayer = (AuthPlayer) this.plugin.getPluginPlayerManager().getPlayer(player);

            if (authPlayer != null && !authPlayer.isLogged()) {
                authPlayer.addTimer();

                if (authPlayer.getTimer() >= timeout) {
                    authPlayer.kickI18n("timeout");
                }
            }
        }
    }

}
