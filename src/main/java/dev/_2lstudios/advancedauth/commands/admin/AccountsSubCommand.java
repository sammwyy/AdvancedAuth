package dev._2lstudios.advancedauth.commands.admin;

import java.util.List;

import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayerData;
import dev._2lstudios.advancedauth.services.AuthService;

@Command(
    name = "accounts", 
    permission = "advancedauth.admin.accounts", 
    aliases = { "alts" }, 
    arguments = { Argument.STRING },
    minArguments = 1,
    usageKey = "admin.accounts.usage"
)
public class AccountsSubCommand extends CommandListener {
    @Override
    public void onExecute(CommandContext ctx) {
        String field = ctx.getArguments().getString(0);
        boolean isAddress = field.contains(".");

        AuthService authService = ctx.getPlugin().getAuthService();
        List<AuthPlayerData> alts = isAddress ? authService.getAltsForAddress(field) : authService.getAltsForPlayer(field);

        if (alts == null) {
            ctx.getExecutor().sendI18nMessage("common.player-not-registered");
        } else {
            String altsText = "";
            int position = 0;

            for (AuthPlayerData alt : alts) {
                position++;

                if (altsText != "") {
                    altsText += "\n";
                }

                altsText += "&9" + position + ".&r &b" + alt.username + " &7(&e" + alt.uuid + "&7)";
            }

            ctx.getExecutor().sendMessage(
                ctx.getExecutor().getI18nMessage("admin.accounts.message")
                    .replace("{alts}", altsText)
                    .replace("{player}", field)
            );
        }
    }
    
}
