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
            player.sendMessage("&eStill downloading your data, please wait...");
            return;
        }

        if (!player.isRegistered()) {
            player.sendMessage("&eYou aren't registered yet.");
            return;
        }

        if (player.isLogged()) {
            player.sendMessage("&eYou are already logged in");
            return;
        }

        if (player.comparePassword(ctx.getArguments().getString(0))) {
            player.login();
            player.sendMessage("&aLogged successfully");
        } else {
            player.sendMessage("&cInvalid password");
        }
    }
}
