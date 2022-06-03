package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.migration.MigrationManager;

@Command(
    name = "migrate", 
    permission = "advancedauth.admin.migrate"
)
public class MigrateSubCommand extends CommandListener {

    private MigrationManager migrationManager;

    public MigrateSubCommand (final AdvancedAuth plugin) {
        this.migrationManager = plugin.getMigrationManager();    
    }

    @Override
    public void onExecute(CommandContext ctx) {
        try {
            ctx.getExecutor().sendMessage("&eStarting migration...");
            int users = migrationManager.startMigration();
            ctx.getExecutor().sendMessage("&aMigration completed (&b" + users + " &ausers migrated)");
        } catch (final Exception e) {
            ctx.getExecutor().sendMessage("&cError migrating: " + e.getMessage());
        }
    }
    
}
