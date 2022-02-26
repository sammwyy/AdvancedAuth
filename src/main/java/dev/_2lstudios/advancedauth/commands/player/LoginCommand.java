package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.Logging;
import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.advancedauth.player.LoginReason;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "login", aliases = {"l"}, silent = true)
public class LoginCommand extends CommandListener {
    private final AdvancedAuth plugin;

    public LoginCommand(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();
        final String password = ctx.getArguments().getString(0);

        if (password == null) {
            player.sendI18nMessage("login.usage");
            return;
        }

        if (!player.isFetched()) {
            player.sendI18nMessage("common.still-downloading");
            return;
        }

        if (!player.isRegistered()) {
            player.sendI18nMessage("register.not-registered");
            return;
        }

        if (player.isLogged()) {
            player.sendI18nMessage("login.already-logged");
            return;
        }

        if (player.comparePassword(password)) {
            player.login(LoginReason.PASSWORD);
            Logging.info(player.getName() + " has been logged in using a password.");
        } else {

            if (this.plugin.getMainConfig().getBoolean("authentication.kick", false)) {
                player.sendI18nMessage("login.wrong-password");
            } else {
                player.getBukkitPlayer().kickPlayer(player.getI18nString("login.wrong-password"));
            }

            Logging.info(player.getName() + " password failed.");
        }
    }
}
