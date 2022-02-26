package dev._2lstudios.advancedauth.commands.admin;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.repository.Repository;
import com.dotphin.milkshakeorm.utils.MapFactory;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayerData;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(
    name = "changepassword", 
    permission = "advancedauth.admin.changepassword", 
    aliases = { "cpw" },
    arguments = { String.class, String.class }
)
public class ChangePasswordSubCommand extends CommandListener {

    private Repository<AuthPlayerData> playerRepository;

    public ChangePasswordSubCommand () {
        this.playerRepository = MilkshakeORM.getRepository(AuthPlayerData.class);
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        String username = ctx.getArguments().getString(0);
        String newPassword = ctx.getArguments().getString(1);

        AuthPlayerData player = this.playerRepository.findOne(MapFactory.create("username", username));

        if (player == null) {
            ctx.getSender().sendI18nMessage("common.player-not-registered");
        } else {
            String hash = AdvancedAuth.getInstance().getCipher().hash(newPassword);
            player.password = hash;
            player.save();

            ctx.getSender().sendMessage(
                ctx.getSender().getI18nString("admin.change-password")
                    .replace("{player}", player.username)
            );
        }
    }
    
}
