package dev._2lstudios.advancedauth.commands;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.commands.admin.SetSpawnSubCommand;

import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "advancedauth")
public class AdvancedAuthCommand extends CommandListener {

    public AdvancedAuthCommand(final AdvancedAuth plugin) {
        this.addSubcommand(new SetSpawnSubCommand(plugin));
    }

    @Override
    public void handle(final CommandContext ctx) throws Exception {
        ctx.getPluginPlayer().sendI18nMessage("admin.help");
    }
}
