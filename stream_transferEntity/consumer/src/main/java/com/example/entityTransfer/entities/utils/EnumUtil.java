package com.example.entityTransfer.entities.utils;

import java.util.Random;

public class EnumUtil {

    private static final Random RANDOM = new Random();

    public static <T extends Enum<?>> T getRandomEnumValue(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[RANDOM.nextInt(enumConstants.length)];
    }

    public static String convertEnumName(String enumName) {
        String[] parts = enumName.toLowerCase().split("_");
        StringBuilder formattedName = new StringBuilder();
        for (String part : parts) {
            formattedName.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1))
                    .append(" ");
        }
        return formattedName.toString().trim();
    }
}