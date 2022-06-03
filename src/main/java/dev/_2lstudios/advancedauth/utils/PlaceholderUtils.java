package dev._2lstudios.advancedauth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.config.Configuration;
import dev._2lstudios.advancedauth.players.AuthPlayer;

public class PlaceholderUtils {
    private static Pattern pattern = Pattern.compile("\\{([^}]+)\\}");

    public static String getPlaceholderValue(String placeholder, AuthPlayer player) {
        AdvancedAuth plugin = player.getPlugin();
        Configuration config = plugin.getConfig();

        switch (placeholder) {
        case "player_name":
            return player.getBukkitPlayer().getName();
        case "player_ip":
            return player.getBukkitPlayer().getAddress().getAddress().toString();
        case "player_locale":
            return player.getLang();
        case "player_accounts":
            return String.valueOf(player.getAlts().size());
        case "password_min":
            return String.valueOf(config.getInt("security.password-min-length"));
        case "password_max":
            return String.valueOf(config.getInt("security.password-max-length"));
        case "max_accounts":
            return String.valueOf(config.getInt("authentication.max-accounts-per-ip"));
        default:
            return placeholder;
        }
    }

    public static String format(String message, AuthPlayer player) {
        Matcher matcher = pattern.matcher(message);
        String result = message;

        while (matcher.find()) {
            String placeholder = matcher.group();
            String placeholderKey = matcher.group(1);
            String placeholderValue = getPlaceholderValue(placeholderKey, player);

            result = result.replaceFirst(Pattern.quote(placeholder), placeholderValue);
        }

        return result;
    }
}
