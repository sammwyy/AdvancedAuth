package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.advancedauth.security.PasswordValidation;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandExecutionTarget;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "register", target = CommandExecutionTarget.ONLY_PLAYER)
public class RegisterCommand extends CommandListener {

    private final AdvancedAuth plugin;

    public RegisterCommand(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();
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

        if (player.getAlts().length >= this.plugin.getMainConfig().getInt("authentication.max-accounts-per-ip", 1)) {
            player.sendI18nMessage("register.too-many-accounts");
            return;
        }

        final String passwordValidation = PasswordValidation.validatePassword(password);
        if (passwordValidation == null) {
            player.register(password);
            player.sendI18nMessage("register.successfully");
        } else {
            player.sendI18nMessage("register." + passwordValidation);
        }
    }
}
