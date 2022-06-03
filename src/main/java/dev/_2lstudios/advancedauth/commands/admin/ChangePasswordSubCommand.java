package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;

@Command(
    name = "changepassword", 
    permission = "advancedauth.admin.changepassword",
    arguments = { Argument.STRING, Argument.STRING },
    minArguments = 2,
    silent = true
)
public class ChangePasswordSubCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(CommandContext ctx) {
        String username = ctx.getArguments().getString(0);
        String newPassword = ctx.getArguments().getString(1);

        if (ctx.getPlugin().getAuthService().changePassword(username, newPassword)) {
            ctx.getExecutor().sendMessage(
                ctx.getExecutor().getI18nMessage("admin.change-password")
                    .replace("{player}", username)
            );
        } else {
            ctx.getExecutor().sendI18nMessage("common.player-not-registered");
        }
    }
    
}
