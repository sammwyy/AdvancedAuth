package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;

@Command(name = "autologin")
public class AutoLoginCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(final CommandContext ctx) {
        final AuthPlayer player = ctx.getPlayer();

        if (!player.isLogged() || player.isGuest()) {
            player.sendI18nMessage("login.not-logged");
            return;
        }
    
        boolean result =  !player.getData().enabledSession;
        player.getData().enabledSession = result;
        player.getData().save();

        if (result) {
            player.sendI18nMessage("autologin.enabled");
            player.createSession();
        } else {
            player.sendI18nMessage("autologin.disabled");
            player.deleteSession();
        }
    }
}
