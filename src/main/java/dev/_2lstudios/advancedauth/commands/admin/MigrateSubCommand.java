package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;

@Command(
    name = "migrate", 
    permission = "advancedauth.admin.migrate"
)
public class MigrateSubCommand extends CommandListener {
    @Override
    public void onExecute(CommandContext ctx) {
        try {
            ctx.getExecutor().sendMessage("&eStarting migration...");
            int users = ctx.getPlugin().getMigrationManager().startMigration();
            ctx.getExecutor().sendMessage("&aMigration completed (&b" + users + " &ausers migrated)");
        } catch (Exception e) {
            ctx.getExecutor().sendMessage("&cError migrating: " + e.getMessage());
        }
    }
    
}
