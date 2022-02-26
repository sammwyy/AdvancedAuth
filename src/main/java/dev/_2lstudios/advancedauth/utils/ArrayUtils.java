package dev._2lstudios.advancedauth.utils;

import java.util.List;
import java.util.Random;

public class ArrayUtils {
    public static String randomItem (final List<String> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
