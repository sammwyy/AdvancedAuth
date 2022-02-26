package dev._2lstudios.advancedauth.commands.admin;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.migration.MigrationManager;
import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(
    name = "migrate", 
    permission = "advancedauth.admin.migrate",
    async = true
)
public class MigrateSubCommand extends CommandListener {

    private MigrationManager migrationManager;

    public MigrateSubCommand (final AdvancedAuth plugin) {
        this.migrationManager = plugin.getMigration();    
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        try {
            ctx.getSender().sendMessage("&eStarting migration...");
            int users = migrationManager.startMigration();
            ctx.getSender().sendMessage("&aMigration completed (&b" + users + " &ausers migrated)");
        } catch (final Exception e) {
            ctx.getSender().sendMessage("&cError migrating: " + e.getMessage());
        }
    }
    
}
