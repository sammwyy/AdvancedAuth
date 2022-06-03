package dev._2lstudios.advancedauth.tasks;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class PlayerDataFetchTask implements Runnable {
    private AdvancedAuth plugin;

    public PlayerDataFetchTask(AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (AuthPlayer player : this.plugin.getPlayerManager().getPlayers()) {
            if (!player.isFetched()) {
                if (player.getLang(false) != null) {
                    player.fetchUserData();

                    if (player.isGuest()) {
                        return;
                    }
                    
                    // Isn't registered
                    if (!player.isRegistered()) {
                        player.sendI18nTitle("register-request.title", "register-request.subtitle");
                    }
    
                    // Is registered but isn't logged
                    else if (!player.isLogged()) {
                        player.sendI18nTitle("login-request.title", "login-request.subtitle");
                    }
                }
            }
        }
    }

}
