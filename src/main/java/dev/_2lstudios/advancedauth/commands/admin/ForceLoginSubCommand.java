package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;
import dev._2lstudios.advancedauth.players.LoginReason;

@Command(
    name = "forcelogin", 
    permission = "advancedauth.admin.forcelogin", 
    arguments = { Argument.PLAYER },
    minArguments = 1
)
public class ForceLoginSubCommand extends CommandListener {
    @Override
    public void onExecute(CommandContext ctx) {
        AuthPlayer player = ctx.getArguments().getPlayer(0);

        if (!player.isRegistered())
            ctx.getExecutor().sendI18nMessage("common.player-not-registered");
        if (player.isLogged()) {
            ctx.getExecutor().sendI18nMessage("admin.force-login.already-logged");
        } else {
            player.login(LoginReason.FORCED);
            ctx.getExecutor().sendI18nMessage("admin.force-login.success");
        }
    }
    
}
