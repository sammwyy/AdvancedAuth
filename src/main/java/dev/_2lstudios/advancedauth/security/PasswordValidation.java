package dev._2lstudios.advancedauth.security;

import java.util.List;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.config.Configuration;

public class PasswordValidation {
    public static String validatePassword(AdvancedAuth plugin, String password) {
        Configuration config = plugin.getConfig();

        int minLength = config.getInt("security.password-min-length", 4);
        int maxLength = config.getInt("security.password-max-length", 32);
        List<String> weakPasswords = config.getStringList("security.password-blacklist");

        if (password == null || password.length() < minLength || password.length() > maxLength) {
            return "password-length";
        }

        if (weakPasswords.contains(password.toLowerCase())) {
            return "password-too-weak";
        }

        return null;
    }
}
