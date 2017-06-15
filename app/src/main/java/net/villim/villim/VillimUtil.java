package net.villim.villim;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static Date dateFromDateString(String dateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long daysBetween(Date one, Date two) { long difference = (one.getTime()-two.getTime())/86400000; return Math.abs(difference); }

}
