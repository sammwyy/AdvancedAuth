package dev._2lstudios.advancedauth.commands.admin;

import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;

import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;

@Command(
    name = "reload", 
    permission = "advancedauth.admin.reload"
)
public class ReloadSubCommand extends CommandListener {
    @Override
    public void onExecute(CommandContext ctx) {
        try {
            ctx.getPlugin().getConfig().load();
            ctx.getPlugin().getMigrationConfig().load();
            ctx.getPlugin().getLanguageManager().loadLanguages();
            ctx.getExecutor().sendI18nMessage("admin.reloaded");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
}
