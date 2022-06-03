package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.config.Configuration;

@Command(
    name = "setspawn", 
    permission = "advancedauth.admin.setspawn"
)
public class SetSpawnSubCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(CommandContext ctx) {
        Configuration config = ctx.getPlugin().getConfig();
        config.setLocation("settings.teleport-spawn.location", ctx.getPlayer().getBukkitPlayer().getLocation());
        config.set("settings.teleport-spawn.enabled", true);
        config.safeSave();
        ctx.getExecutor().sendI18nMessage("admin.spawn-set");
    }
}
