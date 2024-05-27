package com.example.consumer1.models;

import java.util.Random;

public class EnumUtil {

    private static final Random RANDOM = new Random();

    public static <T extends Enum<?>> T getRandomEnumValue(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[RANDOM.nextInt(enumConstants.length)];
    }
}