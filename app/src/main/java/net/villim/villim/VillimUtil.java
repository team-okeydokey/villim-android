package net.villim.villim;

import android.content.Context;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by seongmin on 6/9/17.
 */

public class VillimUtil {

    public static int[] JSONArrayToIntArray(JSONArray array) {
        // Deal with the case of a non-array value.
        if (array == null) { return new int[0]; }

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
        if (array == null) { return new String[0]; }

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

    public static String dateStringFromDate(Context context, Date date) {
        return String.format(context.getString(R.string.date_string_format), date.getYear(), date.getMonth()+1, date.getDate());
    }

    public static int daysBetween(Date one, Date two) { long difference = (one.getTime()-two.getTime())/86400000; return (int) Math.abs(difference); }


    public static String getWeekday(Context context, int weekday) {
        /* Java date은 0부터 6, Calendar 클래스 constant는 1부터 7. */
        switch (weekday + 1) {
            case Calendar.SUNDAY:
                return context.getString(R.string.sunday);
            case Calendar.MONDAY:
                return context.getString(R.string.monday);
            case Calendar.TUESDAY:
                return context.getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return context.getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return context.getString(R.string.thursday);
            case Calendar.FRIDAY:
                return context.getString(R.string.friday);
            case Calendar.SATURDAY:
                return context.getString(R.string.saturday);
            default:
                return context.getString(R.string.sunday);
        }
    }

    public static String currencyStringFromInt(Context context, int code, boolean full) {
        int arrayResourceId = full ? R.array.currencies_full : R.array.currencies_short;
        String[] array = context.getResources().getStringArray(arrayResourceId);
        /* Default to 0 in case of error. */
        if (code >= array.length) {
            return array[0];
        } else {
            return array[code];
        }
    }

    public static String languageStringFromInt(Context context, int code) {
        String[] array = context.getResources().getStringArray(R.array.languages);
        /* Default to 0 in case of error. */
        if (code >= array.length) {
            return array[0];
        } else {
            return array[code];
        }
    }
}
