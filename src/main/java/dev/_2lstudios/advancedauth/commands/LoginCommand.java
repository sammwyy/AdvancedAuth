package dev._2lstudios.advancedauth.commands;

import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "login")
public class LoginCommand extends CommandListener {
    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();

        if (!player.isFetched()) {
            player.sendI18nMessage("common.still-downloading");
            return;
        }

        if (!player.isRegistered()) {
            player.sendI18nMessage("login.not-registered");
            return;
        }

        if (player.isLogged()) {
            player.sendI18nMessage("login.already-logged");
            return;
        }

        if (player.comparePassword(ctx.getArguments().getString(0))) {
            player.login();
            player.sendI18nMessage("login.successfully");
        } else {
            player.sendI18nMessage("login.wrong-password");
        }
    }
}
