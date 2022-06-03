package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.Logging;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;

@Command(
    name = "logout",
    silent = true,
    requireAuth = true
)
public class LogoutCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(final CommandContext ctx) {
        final AuthPlayer player = ctx.getPlayer();
        player.logout();
        player.sendI18nMessage("login.logout");
        Logging.info(player.getName() + " manually logged out.");
    }
}
