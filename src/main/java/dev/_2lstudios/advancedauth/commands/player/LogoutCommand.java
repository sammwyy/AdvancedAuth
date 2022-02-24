package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "logout", silent = true)
public class LogoutCommand extends CommandListener {
    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();

        if (!player.isLogged()) {
            player.sendI18nMessage("login.not-logged");
            return;
        }

        player.logout();
        player.sendI18nMessage("login.logout");
    }
}
