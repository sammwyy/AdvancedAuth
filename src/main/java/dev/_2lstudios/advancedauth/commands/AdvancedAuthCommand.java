package dev._2lstudios.advancedauth.commands;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.commands.admin.SetSpawnSubCommand;

import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "advancedauth", permission = "advancedauth.admin")
public class AdvancedAuthCommand extends CommandListener {

    public AdvancedAuthCommand(final AdvancedAuth plugin) {
        this.addSubcommand(new SetSpawnSubCommand(plugin));
    }

    public void onMissingPermissions(CommandContext ctx, String permission) {
        ctx.getPluginPlayer().sendMessage("&9&lAdvancedAuth &cAuthentication plugin By 2LStudios (Sammwy, LinsaFTW)");
    }

    @Override
    public void handle(final CommandContext ctx) throws Exception {
        ctx.getPluginPlayer().sendI18nMessage("admin.help");
    }
}
