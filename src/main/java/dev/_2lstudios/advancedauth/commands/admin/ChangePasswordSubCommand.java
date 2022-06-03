package dev._2lstudios.advancedauth.commands.admin;


import com.dotphin.milkshake.Milkshake;
import com.dotphin.milkshake.Repository;
import com.dotphin.milkshake.find.FindFilter;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayerData;

@Command(
    name = "changepassword", 
    permission = "advancedauth.admin.changepassword",
    arguments = { Argument.STRING, Argument.STRING },
    minArguments = 2,
    silent = true
)
public class ChangePasswordSubCommand extends CommandListener {
    private Repository<AuthPlayerData> playerRepository;

    public ChangePasswordSubCommand () {
        this.playerRepository = Milkshake.getRepository(AuthPlayerData.class);
    }

    @Override
    public void onExecuteByPlayer(CommandContext ctx) {
        String username = ctx.getArguments().getString(0);
        String newPassword = ctx.getArguments().getString(1);

        AuthPlayerData player = this.playerRepository.findOne(new FindFilter("username", username.toLowerCase()));

        if (player == null) {
            ctx.getExecutor().sendI18nMessage("common.player-not-registered");
        } else {
            String hash = AdvancedAuth.getInstance().getCipher().hash(newPassword);
            player.password = hash;
            player.save();

            ctx.getExecutor().sendMessage(
                ctx.getExecutor().getI18nMessage("admin.change-password")
                    .replace("{player}", player.username)
            );
        }
    }
    
}
