package dev._2lstudios.advancedauth.bukkit.commands.player;

import dev._2lstudios.advancedauth.bukkit.Logging;
import dev._2lstudios.advancedauth.bukkit.player.AuthPlayer;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandExecutionTarget;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "unregister", aliases={"unreg"}, silent = true, target = CommandExecutionTarget.ONLY_PLAYER)
public class UnregisterCommand extends CommandListener {
    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();
        final String password = ctx.getArguments().getString(0);

        if (!player.isLogged() || player.isGuest()) {
            player.sendI18nMessage("login.not-logged");
            return;
        }

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
