package dev._2lstudios.advancedauth.commands.admin;

import com.dotphin.milkshake.Milkshake;
import com.dotphin.milkshake.Repository;
import com.dotphin.milkshake.find.FindFilter;

import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayerData;
import dev._2lstudios.advancedauth.services.GeoIPService;

@Command(
    name = "ip", 
    permission = "advancedauth.admin.ip", 
    arguments = { Argument.STRING }
)
public class IPSubCommand extends CommandListener {

    private Repository<AuthPlayerData> playerRepository;

    public IPSubCommand () {
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
            String lastIPCountry = GeoIPService.getCountry(lastIP);

            String registeredIP = player.registrationIP;
            String registeredCountry = GeoIPService.getCountry(registeredIP);

            ctx.getExecutor().sendMessage(
                ctx.getExecutor().getI18nMessage("admin.ip")
                    .replace("{player}", player.username)
                    .replace("{last_login_ip}", lastIP)
                    .replace("{registration_ip}", registeredIP)
                    .replace("{last_country}", lastIPCountry)
                    .replace("{registration_country}", registeredCountry)
            );
        }
    }
    
}
