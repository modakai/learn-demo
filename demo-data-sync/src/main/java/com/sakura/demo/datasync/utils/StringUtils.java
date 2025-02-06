package com.sakura.demo.datasync.utils;

public class StringUtils {
    public static String toCamelCase(String underscoreName) {
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < underscoreName.length(); i++) {
            char currentChar = underscoreName.charAt(i);
            if (currentChar == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpperCase = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            }
        }
        return result.toString();
    }

    public static String toPascalCase(String underscoreName) {
        String camelCase = toCamelCase(underscoreName);
        return camelCase.substring(0, 1).toUpperCase() + camelCase.substring(1);
    }
}