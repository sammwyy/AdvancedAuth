package dev._2lstudios.advancedauth.commands.admin;

import java.util.List;

import com.dotphin.milkshake.Milkshake;
import com.dotphin.milkshake.Repository;
import com.dotphin.milkshake.find.FindFilter;

import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayerData;

@Command(
    name = "accounts", 
    permission = "advancedauth.admin.accounts", 
    aliases = { "alts" }, 
    arguments = { Argument.STRING }
)
public class AccountsSubCommand extends CommandListener {

    private Repository<AuthPlayerData> playerRepository;

    public AccountsSubCommand () {
        this.playerRepository = Milkshake.getRepository(AuthPlayerData.class);
    }

    @Override
    public void onExecute(CommandContext ctx) {
        String username = ctx.getArguments().getString(0);
        AuthPlayerData player = this.playerRepository.findOne(new FindFilter("username", username.toLowerCase()));

        if (player == null) {
            ctx.getExecutor().sendI18nMessage("common.player-not-registered");
        } else {
            String lastIP = player.lastLoginIP;
            List<AuthPlayerData> alts = this.playerRepository.findMany(new FindFilter("lastLoginIP", lastIP));

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
                ctx.getExecutor().getI18nMessage("admin.accounts")
                    .replace("{alts}", altsText)
                    .replace("{player}", player.username)
            );
        }
    }
    
}
