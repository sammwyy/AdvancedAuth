package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.Logging;
import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;
import dev._2lstudios.advancedauth.security.PasswordValidation;

@Command(
    name = "register",
    arguments = { Argument.STRING },
    minArguments = 1,
    silent = true
)
public class RegisterCommand extends CommandListener {

    private final AdvancedAuth plugin;

    public RegisterCommand(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onExecuteByPlayer(final CommandContext ctx) {
        final AuthPlayer player = ctx.getPlayer();
        final String password = ctx.getArguments().getString(0);

        if (!player.isFetched()) {
            player.sendI18nMessage("common.still-downloading");
            return;
        }

        if (player.isRegistered()) {
            player.sendI18nMessage("register.already-registered");
            return;
        }

        if (password == null) {
            player.sendI18nMessage("register.usage");
            return;
        }

        if (player.getAlts().size() >= this.plugin.getConfig().getInt("authentication.max-accounts-per-ip", 1)) {
            player.sendI18nMessage("register.too-many-accounts");
            Logging.info(player.getName() + " tried to register but has already exceeded the limit of registered accounts.");
            return;
        }

        final String passwordValidation = PasswordValidation.validatePassword(password);
        if (passwordValidation == null) {
            player.register(password);
            player.sendI18nMessage("register.successfully");
            Logging.info(player.getName() + " has been registered.");
        } else {
            player.sendI18nMessage("register." + passwordValidation);
        }
    }
}
