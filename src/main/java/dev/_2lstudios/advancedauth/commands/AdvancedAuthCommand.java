package dev._2lstudios.advancedauth.commands;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.commands.admin.AccountsSubCommand;
import dev._2lstudios.advancedauth.commands.admin.ChangePasswordSubCommand;
import dev._2lstudios.advancedauth.commands.admin.ForceLoginSubCommand;
import dev._2lstudios.advancedauth.commands.admin.IPSubCommand;
import dev._2lstudios.advancedauth.commands.admin.MigrateSubCommand;
import dev._2lstudios.advancedauth.commands.admin.SetSpawnSubCommand;
import dev._2lstudios.advancedauth.commands.admin.UnregisterSubCommand;

@Command(
    name = "advancedauth", 
    permission = "advancedauth.admin"
)
public class AdvancedAuthCommand extends CommandListener {
    public AdvancedAuthCommand(final AdvancedAuth plugin) {
        this.addSubcommand(new AccountsSubCommand());
        this.addSubcommand(new ChangePasswordSubCommand());
        this.addSubcommand(new ForceLoginSubCommand());
        this.addSubcommand(new IPSubCommand());
        this.addSubcommand(new MigrateSubCommand(plugin));
        this.addSubcommand(new SetSpawnSubCommand(plugin));
        this.addSubcommand(new UnregisterSubCommand());
    }

    @Override
    public void onMissingPermission(CommandContext ctx) {
        ctx.getExecutor().sendMessage("&9&lAdvancedAuth &cAuthentication plugin By 2LStudios (Sammwy, LinsaFTW)");
    }

    @Override
    public void onExecute(final CommandContext ctx) {
        ctx.getExecutor().sendI18nMessage("admin.help");
    }
}
