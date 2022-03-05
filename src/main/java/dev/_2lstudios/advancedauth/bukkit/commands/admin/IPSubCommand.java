package dev._2lstudios.advancedauth.bukkit.commands.admin;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.repository.Repository;
import com.dotphin.milkshakeorm.utils.MapFactory;

import dev._2lstudios.advancedauth.bukkit.player.AuthPlayerData;
import dev._2lstudios.advancedauth.common.services.GeoIPService;
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
        AuthPlayerData player = this.playerRepository.findOne(MapFactory.create("username", username.toLowerCase()));

        if (player == null) {
            ctx.getSender().sendI18nMessage("common.player-not-registered");
        } else {
            String lastIP = player.lastLoginIP;
            String lastIPCountry = GeoIPService.getCountry(lastIP);

            String registeredIP = player.registrationIP;
            String registeredCountry = GeoIPService.getCountry(registeredIP);

            ctx.getSender().sendMessage(
                ctx.getSender().getI18nString("admin.ip")
                    .replace("{player}", player.username)
                    .replace("{last_login_ip}", lastIP)
                    .replace("{registration_ip}", registeredIP)
                    .replace("{last_country}", lastIPCountry)
                    .replace("{registration_country}", registeredCountry)
            );
        }
    }
    
}
