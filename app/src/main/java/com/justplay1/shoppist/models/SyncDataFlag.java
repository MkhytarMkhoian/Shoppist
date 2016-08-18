package com.justplay1.shoppist.models;

/**
 * Created by Mkhytar on 28.10.2015.
 */
public class SyncDataFlag {
    public static final int ALL = 0;
    public static final int CATEGORIES = 1;
    public static final int UNITS = 2;
    public static final int CURRENCIES = 3;
    public static final int SHOPPING_LISTS = 4;
    public static final int SHOPPING_LIST_ITEMS = 5;
    public static final int TODO_LISTS = 6;
    public static final int TODO_LIST_ITEMS = 7;
    public static final int GOODS = 8;

    public static String combineFlags(int... flags) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < flags.length; i++) {
            builder.append(flags[i]);
            if (i != flags.length - 1) {
                builder.append("|");
            }
        }
        return builder.toString();
    }

    public static int[] parseFlags(String flags) {
        String[] strings = flags.split("|");
        int[] result = new int[strings.length];

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].isEmpty()) continue;
            result[i] = Integer.valueOf(strings[i]);
        }
        return result;
    }
}
