package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.Logging;
import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;
import dev._2lstudios.advancedauth.security.PasswordValidation;

@Command(
    name = "changepassword",
    arguments = { Argument.STRING, Argument.STRING },
    minArguments = 2,
    requireAuth = true,
    silent = true
)
public class ChangePasswordCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(CommandContext ctx) {
        AuthPlayer player = ctx.getPlayer();
        String oldPassword = ctx.getArguments().getString(0);
        String newPassword = ctx.getArguments().getString(1);

        if (!player.comparePassword(oldPassword)) {
            player.sendI18nMessage("login.wrong-password");
            Logging.info(player.getName() + " has tried to change his password but failed to verify the old one.");
            return;
        }

        String passwordValidation = PasswordValidation.validatePassword(newPassword);
        if (passwordValidation == null) {
            player.setPassword(newPassword);
            player.sendI18nMessage("changepassword.successfully");
            Logging.info(player.getName() + " has changed his password.");
        } else {
            player.sendI18nMessage("register." + passwordValidation);
        }
    }
}
