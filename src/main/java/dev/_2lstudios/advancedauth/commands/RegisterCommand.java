package dev._2lstudios.advancedauth.commands;

import java.util.List;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayer;
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

        final int minLength = this.plugin.getMainConfig().getInt("security.password-min-length", 4);
        final int maxLength = this.plugin.getMainConfig().getInt("security.password-max-length", 32);

        if (password.length() < minLength || password.length() > maxLength) {
            player.sendI18nMessage("register.password-length");
            return;
        }

        final List<String> weakPasswords = this.plugin.getMainConfig().getStringList("security.password-blacklist");
        if (weakPasswords.contains(password.toLowerCase())) {
            player.sendI18nMessage("register.password-too-weak");
            return;
        }

        player.register(ctx.getArguments().getString(0));
        player.sendI18nMessage("register.successfully");
    }
}
