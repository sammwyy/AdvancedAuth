package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;

@Command(
    name = "unregister", 
    permission = "advancedauth.admin.unregister", 
    arguments = { Argument.STRING },
    minArguments = 1,
    usageKey = "admin.unregister.usage"
)
public class UnregisterSubCommand extends CommandListener {
    @Override
    public void onExecute(CommandContext ctx) {
        String username = ctx.getArguments().getString(0);

        if (ctx.getPlugin().getAuthService().unregister(username)) {
            ctx.getExecutor().sendMessage(
                ctx.getExecutor().getI18nMessage("admin.unregister.unregistered")
                    .replace("{player}", username)
            );
        } else {
            ctx.getExecutor().sendI18nMessage("common.player-not-registered");
        }
    }
}
