package dev._2lstudios.advancedauth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.player.AuthPlayer;
import dev._2lstudios.jelly.config.Configuration;

public class PlaceholderUtils {

    private final static Pattern pattern = Pattern.compile("\\{([^}]+)\\}");

    public static String getPlaceholderValue(final String placeholder, final AuthPlayer player) {
        final AdvancedAuth plugin = AdvancedAuth.getInstance();
        final Configuration config = plugin.getMainConfig();

        switch (placeholder) {
        case "player_name":
            return player.getBukkitPlayer().getName();
        case "player_ip":
            return player.getBukkitPlayer().getAddress().getAddress().toString();
        case "player_locale":
            return player.getLocale();
        case "player_accounts":
            return String.valueOf(player.getAlts().length);
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

    public static String format(String message, final AuthPlayer player) {
        final Matcher matcher = pattern.matcher(message);
        String result = message;

        while (matcher.find()) {
            final String placeholder = matcher.group();
            final String placeholderKey = matcher.group(1);
            final String placeholderValue = getPlaceholderValue(placeholderKey, player);

            result = result.replaceFirst(Pattern.quote(placeholder), placeholderValue);
        }

        return result;
    }
}
