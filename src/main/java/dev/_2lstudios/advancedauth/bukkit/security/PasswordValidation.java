package dev._2lstudios.advancedauth.bukkit.security;

import java.util.List;

import dev._2lstudios.advancedauth.bukkit.AdvancedAuth;
import dev._2lstudios.jelly.config.Configuration;

public class PasswordValidation {
    public static String validatePassword(final String password) {
        final Configuration config = AdvancedAuth.getInstance().getMainConfig();

        final int minLength = config.getInt("security.password-min-length", 4);
        final int maxLength = config.getInt("security.password-max-length", 32);
        final List<String> weakPasswords = config.getStringList("security.password-blacklist");

        if (password == null || password.length() < minLength || password.length() > maxLength) {
            return "password-length";
        }

        if (weakPasswords.contains(password.toLowerCase())) {
            return "password-too-weak";
        }

        return null;
    }
}
