package net.villim.villim;

import org.json.JSONArray;

/**
 * Created by seongmin on 6/9/17.
 */

public class VillimUtil {

    public static int[] JSONArrayToIntArray(JSONArray array) {
        // Deal with the case of a non-array value.
        if (array == null) { /*...*/ }

        // Create an int array to accomodate the numbers.
        int[] numbers = new int[array.length()];

        // Extract numbers from JSON array.
        for (int i = 0; i < array.length(); ++i) {
            numbers[i] = array.optInt(i);
        }

        return numbers;
    }

    public static String[] JSONArrayToStringArray(JSONArray array) {
        // Deal with the case of a non-array value.
        if (array == null) { /*...*/ }

        // Create an int array to accomodate the numbers.
        String[] strings = new String[array.length()];

        // Extract numbers from JSON array.
        for (int i = 0; i < array.length(); ++i) {
            strings[i] = array.optString(i);
        }

        return strings;
    }

}
