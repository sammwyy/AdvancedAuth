package dev._2lstudios.advancedauth.commands;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.errors.BadArgumentException;
import dev._2lstudios.advancedauth.errors.MaterialNotFoundException;
import dev._2lstudios.advancedauth.errors.PlayerOfflineException;
import dev._2lstudios.advancedauth.errors.SoundNotFoundException;
import dev._2lstudios.advancedauth.errors.WorldNotFoundException;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class CommandContext {
    private AdvancedAuth plugin;
    private CommandExecutor executor;
    private CommandArguments arguments;

    public CommandContext(AdvancedAuth plugin, CommandSender sender, Argument[] requiredArguments) {
        if (sender instanceof Player) {
            this.executor = plugin.getPlayerManager().getPlayer((Player) sender);
        } else {
            this.executor = new CommandExecutor(plugin, sender);
        }

        this.plugin = plugin;
        this.arguments = new CommandArguments(plugin, requiredArguments);
    }

    public void parseArguments(String[] args) throws BadArgumentException, PlayerOfflineException, WorldNotFoundException, MaterialNotFoundException, SoundNotFoundException {
        this.arguments.parse(args);
    }

    public AdvancedAuth getPlugin() {
        return this.plugin;
    }

    public CommandExecutor getExecutor() {
        return this.executor;
    }

    public AuthPlayer getPlayer() {
        return (AuthPlayer) this.executor;
    }

    public boolean isPlayer() {
        return this.executor.isPlayer();
    }

    public CommandArguments getArguments() {
        return this.arguments;
    }
}