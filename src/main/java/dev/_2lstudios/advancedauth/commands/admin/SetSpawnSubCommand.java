package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;

@Command(
    name = "setspawn", 
    permission = "advancedauth.admin.setspawn"
)
public class SetSpawnSubCommand extends CommandListener {
    private final AdvancedAuth plugin;

    public SetSpawnSubCommand (final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onExecuteByPlayer(final CommandContext ctx) {
        ctx.getExecutor().sendI18nMessage("admin.spawn-set");
        this.plugin.getConfig().setLocation("settings.teleport-spawn.location", ctx.getPlayer().getBukkitPlayer().getLocation());
        this.plugin.getConfig().set("settings.teleport-spawn.enabled", true);
        this.plugin.getConfig().safeSave();
    }
}
