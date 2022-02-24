package dev._2lstudios.advancedauth.commands.admin;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.repository.Repository;
import com.dotphin.milkshakeorm.utils.MapFactory;

import dev._2lstudios.advancedauth.player.AuthPlayerData;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(
    name = "accounts", 
    permission = "advancedauth.admin.accounts", 
    aliases = { "alts" }, 
    arguments = { String.class }
)
public class AccountsSubCommand extends CommandListener {

    private Repository<AuthPlayerData> playerRepository;

    public AccountsSubCommand () {
        this.playerRepository = MilkshakeORM.getRepository(AuthPlayerData.class);
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        String username = ctx.getArguments().getString(0);
        AuthPlayerData player = this.playerRepository.findOne(MapFactory.create("username", username));

        if (player == null) {
            ctx.getPluginPlayer().sendI18nMessage("common.player-not-registered");
        } else {
            String lastIP = player.lastLoginIP;
            AuthPlayerData[] alts = this.playerRepository.findMany(MapFactory.create("lastLoginIP", lastIP));

            String altsText = "";
            int position = 0;

            for (AuthPlayerData alt : alts) {
                position++;

                if (altsText != "") {
                    altsText += "\n";
                }

                altsText += "&9" + position + ".&r &b" + alt.username + " &7(&e" + alt.uuid + "&7)";
            }

            ctx.getPluginPlayer().sendMessage(
                ctx.getPluginPlayer().getI18nString("admin.accounts")
                    .replace("{alts}", altsText)
                    .replace("{player}", player.username)
            );
        }
    }
    
}
