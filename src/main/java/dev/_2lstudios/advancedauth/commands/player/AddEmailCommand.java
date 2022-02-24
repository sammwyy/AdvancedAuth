package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.advancedauth.security.EmailValidation;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "addemail", silent = true)
public class AddEmailCommand extends CommandListener {
    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();
        final String email = ctx.getArguments().getString(0);

        if (!player.isLogged()) {
            player.sendI18nMessage("login.already-logged");
            return;
        }

        if (email == null) {
            player.sendI18nMessage("addemail.usage");
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
    }
}
