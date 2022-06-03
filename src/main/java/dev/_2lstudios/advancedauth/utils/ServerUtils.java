package dev._2lstudios.advancedauth.utils;

import java.lang.reflect.Method;

enum VALUE {
    YES, NO, UNKNOWN
}

public class ServerUtils {
    private static VALUE HAS_PLAYER_GET_LOCALE_METHOD = VALUE.UNKNOWN;
    
    public static boolean hasPlayerGetLocaleAPI() {
        if (HAS_PLAYER_GET_LOCALE_METHOD == VALUE.UNKNOWN) {
            try {
                Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
                Method method = playerClass.getMethod("getLocale");
                HAS_PLAYER_GET_LOCALE_METHOD = method == null ? VALUE.NO : VALUE.YES;
            } catch (Exception ignored) {
                HAS_PLAYER_GET_LOCALE_METHOD = VALUE.NO;
            }
        }

        return HAS_PLAYER_GET_LOCALE_METHOD == VALUE.YES;
    }
}
