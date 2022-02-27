package dev._2lstudios.advancedauth.bukkit.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidation {
    private static Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");

    public static boolean isValidEmailFormat(final String email) {
        final Matcher mat = emailPattern.matcher(email);
        return mat.matches();
    }
}
