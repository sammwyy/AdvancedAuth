package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "setspawn")
public class SetSpawnSubCommand extends CommandListener {

    private final AdvancedAuth plugin;

    public SetSpawnSubCommand (final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(final CommandContext ctx) throws Exception {
        ctx.getPluginPlayer().sendI18nMessage("admin.spawn-set");
        this.plugin.getMainConfig().setLocation("settings.teleport-spawn.location", ctx.getPlayer().getLocation());
        this.plugin.getMainConfig().set("settings.teleport-spawn.enabled", true);
        this.plugin.getMainConfig().save();
    }
}
