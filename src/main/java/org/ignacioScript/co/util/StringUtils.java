package org.ignacioScript.co.util;

public class StringUtils {


    public static String capitalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String[] words = name.toLowerCase().trim().split("\\s+");
        StringBuilder capitalizedName = new StringBuilder();

         for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return capitalizedName.toString().trim();
    }

    public static String toLowerCase(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        return text.toLowerCase();
    }
}
