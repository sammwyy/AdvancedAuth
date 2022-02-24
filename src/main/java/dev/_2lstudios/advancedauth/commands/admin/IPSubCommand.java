package dev._2lstudios.advancedauth.commands.admin;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.repository.Repository;
import com.dotphin.milkshakeorm.utils.MapFactory;

import dev._2lstudios.advancedauth.player.AuthPlayerData;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(
    name = "ip", 
    permission = "advancedauth.admin.ip", 
    arguments = { String.class }
)
public class IPSubCommand extends CommandListener {

    private Repository<AuthPlayerData> playerRepository;

    public IPSubCommand () {
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
            String registeredIP = player.registrationIP;

            ctx.getPluginPlayer().sendMessage(
                ctx.getPluginPlayer().getI18nString("admin.ip")
                    .replace("{player}", player.username)
                    .replace("{last_login_ip}", lastIP)
                    .replace("{registration_ip}", registeredIP)
            );
        }
    }
    
}