package dev._2lstudios.advancedauth.security;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class ConsoleFilterHelper {
    private static String ISSUED_COMMAND_TEXT = "issued server command: /";

    public static boolean hasSilentCommand(String text) {
        if (text.contains(ISSUED_COMMAND_TEXT)) {
            String command = text.split(ISSUED_COMMAND_TEXT, 2)[1];
            if (command.contains(" ")) {
                command = command.split(" ")[0];
            }
            
            return AdvancedAuth.getInstance().getSilentCommands().contains(command.toLowerCase());
        }

        return false;
    }
}
