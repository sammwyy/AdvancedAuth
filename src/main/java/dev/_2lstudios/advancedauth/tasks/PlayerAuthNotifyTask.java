package dev._2lstudios.advancedauth.tasks;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayer;

public class PlayerAuthNotifyTask implements Runnable {

    private final AdvancedAuth plugin;

    public PlayerAuthNotifyTask(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            final AuthPlayer authPlayer = (AuthPlayer) this.plugin.getPluginPlayerManager().getPlayer(player);

            if (authPlayer != null && authPlayer.isFetched()) {
                // Isn't registered
                if (!authPlayer.isRegistered()) {
                    authPlayer.sendI18nMessage("register.message");
                    return;
                }

                // Is registered but isn't logged
                if (!authPlayer.isLogged()) {
                    authPlayer.sendI18nMessage("login.message");
                }
            }
        }
    }

}
