package dev._2lstudios.advancedauth.tasks;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class PlayerDataFetchTask implements Runnable {
    private AdvancedAuth plugin;

    public PlayerDataFetchTask(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (AuthPlayer player : this.plugin.getPlayerManager().getPlayers()) {
            if (player.isFetched()) {
                if (player.getLang() != null) {
                    player.fetchUserData();
                }
            }
        }
    }

}
