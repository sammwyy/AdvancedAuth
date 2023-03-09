package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;

@Command(name = "autologin", requireAuth = true)
public class AutoLoginCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(CommandContext ctx) {
        AuthPlayer player = ctx.getPlayer();

        boolean result = !player.getData().enabledSession;
        player.getData().enabledSession = result;
        player.getData().save();

        if (result) {
            player.sendI18nMessage("autologin.enabled");
            player.createSession();
        } else {
            player.sendI18nMessage("autologin.disabled");
            player.deleteSession();
        }
    }
}
