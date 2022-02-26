package dev._2lstudios.advancedauth.commands.admin;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.repository.Repository;
import com.dotphin.milkshakeorm.utils.MapFactory;

import dev._2lstudios.advancedauth.player.AuthPlayerData;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(
    name = "unregister", 
    permission = "advancedauth.admin.unregister", 
    arguments = { String.class }
)
public class UnregisterSubCommand extends CommandListener {

    private Repository<AuthPlayerData> playerRepository;

    public UnregisterSubCommand () {
        this.playerRepository = MilkshakeORM.getRepository(AuthPlayerData.class);
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        String username = ctx.getArguments().getString(0);
        AuthPlayerData player = this.playerRepository.findOne(MapFactory.create("username", username));

        if (player == null) {
            ctx.getPluginPlayer().sendI18nMessage("common.player-not-registered");
        } else {
            player.delete();
            ctx.getPluginPlayer().sendMessage(
                ctx.getPluginPlayer().getI18nString("admin.unregistered")
                    .replace("{player}", player.username)
            );
        }
    }
    
}
