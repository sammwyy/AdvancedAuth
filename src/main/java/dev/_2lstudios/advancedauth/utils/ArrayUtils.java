package dev._2lstudios.advancedauth.utils;

import java.util.List;
import java.util.Random;

public class ArrayUtils {
  public static String[] removeFirstElement(String[] arr) {
    String newArr[] = new String[arr.length - 1];
    for (int i = 1; i < arr.length; i++) {
      newArr[i - 1] = arr[i];
    }
    return newArr;
  }

  public static String randomItem(List<String> list) {
    Random rand = new Random();
    return list.get(rand.nextInt(list.size()));
  }
}