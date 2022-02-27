package dev._2lstudios.advancedauth.bukkit.commands.admin;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "setspawn", permission = "advancedauth.admin.setspawn")
public class SetSpawnSubCommand extends CommandListener {

    private final AdvancedAuth plugin;

    public SetSpawnSubCommand (final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(final CommandContext ctx) throws Exception {
        ctx.getSender().sendI18nMessage("admin.spawn-set");
        this.plugin.getMainConfig().setLocation("settings.teleport-spawn.location", ctx.getPlayer().getLocation());
        this.plugin.getMainConfig().set("settings.teleport-spawn.enabled", true);
        this.plugin.getMainConfig().save();
    }
}
