package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "autologin")
public class AutoLoginCommand extends CommandListener {
    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();
        if (player.getData().enabledSession) {
            player.getData().enabledSession = !player.getData().enabledSession;
            player.getData().save();

            if (player.getData().enabledSession) {
                player.sendI18nMessage("autologin.enabled");
                player.createSession();
            } else {
                player.sendI18nMessage("autologin.disabled");
                player.deleteSession();
            }
        }
    }
}
