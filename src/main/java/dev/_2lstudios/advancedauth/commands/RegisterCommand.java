package dev._2lstudios.advancedauth.commands;

import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "register")
public class RegisterCommand extends CommandListener {
    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();

        if (!player.isFetched()) {
            player.sendMessage("&eStill downloading your data, please wait...");
            return;
        }

        if (player.isRegistered()) {
            player.sendMessage("&eYou are already registered");
            return;
        }

        player.register(ctx.getArguments().getString(0));
        player.sendMessage("&aRegistered successfully");
    }
}
