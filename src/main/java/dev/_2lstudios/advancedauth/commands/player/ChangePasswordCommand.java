package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.advancedauth.security.PasswordValidation;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "changepassword")
public class ChangePasswordCommand extends CommandListener {
    @Override
    public void handle(final CommandContext ctx) throws Exception {
        final AuthPlayer player = (AuthPlayer) ctx.getPluginPlayer();
        final String oldPassword = ctx.getArguments().getString(0);
        final String newPassword = ctx.getArguments().getString(1);

        if (!player.isLogged()) {
            player.sendI18nMessage("login.already-logged");
            return;
        }

        if (oldPassword == null || newPassword == null) {
            player.sendI18nMessage("changepassword.usage");
            return;
        }

        if (!player.comparePassword(oldPassword)) {
            player.sendI18nMessage("login.wrong-password");
            return;
        }

        final String passwordValidation = PasswordValidation.validatePassword(newPassword);
        if (passwordValidation == null) {
            player.setPassword(newPassword);
            player.sendI18nMessage("changepassword.successfully");
        } else {
            player.sendI18nMessage("register." + passwordValidation);
        }
    }
}
