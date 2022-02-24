package dev._2lstudios.advancedauth.commands.admin;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.advancedauth.player.LoginReason;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;
import dev._2lstudios.jelly.errors.PlayerOfflineException;

@Command(
    name = "forcelogin", 
    permission = "advancedauth.admin.forcelogin", 
    arguments = { Player.class }
)
public class ForceLoginSubCommand extends CommandListener {

    private final AdvancedAuth plugin;

    public ForceLoginSubCommand () {
        this.plugin = AdvancedAuth.getInstance();
    }

    @Override
    public void onPlayerOffline(CommandContext ctx, PlayerOfflineException e) {
        ctx.getPluginPlayer().sendMessage(
            ctx.getPluginPlayer().getI18nString("common.player-not-registered")
                .replace("{player}", e.getPlayer())
        );
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        Player bukkitPlayer = ctx.getArguments().getPlayer(0);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            this.onPlayerOffline(ctx, null);
            return;
        }

        AuthPlayer player = (AuthPlayer) plugin.getPluginPlayerManager().getPlayer(bukkitPlayer);
        if (!player.isRegistered())
            ctx.getPluginPlayer().sendI18nMessage("common.player-not-registered");
        if (player.isLogged()) {
            ctx.getPluginPlayer().sendI18nMessage("admin.force-login.already-logged");
        } else {
            player.login(LoginReason.FORCED);
            ctx.getPluginPlayer().sendI18nMessage("admin.force-login.success");
        }
    }
    
}
