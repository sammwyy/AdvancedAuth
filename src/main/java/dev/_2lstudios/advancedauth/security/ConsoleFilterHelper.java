package dev._2lstudios.advancedauth.security;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class ConsoleFilterHelper {
    private String ISSUED_COMMAND_TEXT = "issued server command: /";

    private AdvancedAuth plugin;

    public ConsoleFilterHelper(AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    public boolean hasSilentCommand(String text) {
        if (text.contains(ISSUED_COMMAND_TEXT)) {
            String command = text.split(ISSUED_COMMAND_TEXT, 2)[1];
            if (command.contains(" ")) {
                command = command.split(" ")[0];
            }
            
            return this.plugin.getSilentCommands().contains(command.toLowerCase());
        }

        return false;
    }
}
