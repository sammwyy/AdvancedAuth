package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.Logging;
import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;
import dev._2lstudios.advancedauth.security.EmailValidation;

@Command(
    name = "addemail",
    arguments = { Argument.STRING },
    minArguments = 1,
    silent = true
)
public class AddEmailCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(final CommandContext ctx) {
        final AuthPlayer player = (AuthPlayer) ctx.getPlayer();
        final String email = ctx.getArguments().getString(0);

        if (!player.isLogged() || player.isGuest()) {
            player.sendI18nMessage("login.not-logged");
            return;
        }

        if (!player.isFetched()) {
            player.sendI18nMessage("common.still-downloading");
            return;
        }

        if (!EmailValidation.isValidEmailFormat(email)) {
            player.sendI18nMessage("addemail.bad-format");
            return;
        }

        player.setEmail(email);
        player.sendI18nMessage("addemail.successfully");
        Logging.info(player.getName() + " has changed his email.");
    }
}
