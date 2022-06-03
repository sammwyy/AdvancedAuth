package dev._2lstudios.advancedauth.commands.admin;

import com.dotphin.milkshake.Milkshake;
import com.dotphin.milkshake.Repository;
import com.dotphin.milkshake.find.FindFilter;

import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayerData;

@Command(
    name = "unregister", 
    permission = "advancedauth.admin.unregister", 
    arguments = { Argument.STRING },
    minArguments = 1,
    silent = true
)
public class UnregisterSubCommand extends CommandListener {
    private Repository<AuthPlayerData> playerRepository;

    public UnregisterSubCommand () {
        this.playerRepository = Milkshake.getRepository(AuthPlayerData.class);
    }

    @Override
    public void onExecute(CommandContext ctx) {
        String username = ctx.getArguments().getString(0);
        AuthPlayerData player = this.playerRepository.findOne(new FindFilter("username", username.toLowerCase()));

        if (player == null) {
            ctx.getExecutor().sendI18nMessage("common.player-not-registered");
        } else {
            player.delete();
            ctx.getExecutor().sendMessage(
                ctx.getExecutor().getI18nMessage("admin.unregistered")
                    .replace("{player}", player.username)
            );
        }
    }
    
}
