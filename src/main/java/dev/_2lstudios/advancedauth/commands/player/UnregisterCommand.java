package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.Logging;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;

@Command(
    name = "unregister",
    silent = true,
    requireAuth = true
)
public class UnregisterCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(final CommandContext ctx) {
        final AuthPlayer player = (AuthPlayer) ctx.getPlayer();
        final String password = ctx.getArguments().getString(0);

        if (password == null) {
            player.sendI18nMessage("unregister.usage");
            return;
        }

        if (!player.comparePassword(password)) {
            player.sendI18nMessage("login.wrong-password");
            return;
        }

        player.unregister();
        player.sendI18nMessage("unregister.successfully");
        Logging.info(player.getName() + " has deregistered his account.");
    }
}
