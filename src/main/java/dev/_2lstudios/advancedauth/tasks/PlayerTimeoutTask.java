package dev._2lstudios.advancedauth.tasks;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class PlayerTimeoutTask implements Runnable {

    private final AdvancedAuth plugin;
    private final long timeout;

    public PlayerTimeoutTask(final AdvancedAuth plugin) {
        this.plugin = plugin;
        this.timeout = this.plugin.getConfig().getInt("authentication.timeout", 30);
    }

    @Override
    public void run() {
        for (AuthPlayer player : this.plugin.getPlayerManager().getPlayers()) {
            if (!player.isLogged()) {
                player.addTimer();

                if (player.getTimer() >= timeout) {
                    player.kickI18n("timeout");
                }
            }
        }
    }

}
