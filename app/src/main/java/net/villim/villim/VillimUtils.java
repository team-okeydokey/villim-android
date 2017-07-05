package net.villim.villim;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.util.DisplayMetrics;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;


/**
 * Created by seongmin on 6/9/17.
 */

public class VillimUtils {

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
            String onlyDate = dateString.split(" ")[0];
            date = df.parse(onlyDate);
            date.setYear(date.getYear() + 1900);
            date.setMonth(date.getMonth() + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateStringFromDate(Context context, Date date) {
        return String.format(context.getString(R.string.date_string_format), date.getYear()+1900, date.getMonth()+1, date.getDate());
    }

    public static int daysBetween(Date one, Date two) { long difference = (one.getTime()-two.getTime())/86400000; return (int) Math.abs(difference); }


    public static int daysUntilEndOfMonth(Date date) {
        int endDate;
        switch (date.getMonth()) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                endDate = 31; break;
            case 4:
            case 6:
            case 9:
            case 11:
                endDate = 30; break;
            case 2:
                endDate = 28; break;
            default:
                endDate = 31; break;
        }
        return endDate - date.getDate();
    }

    public static int daysFromStartOfMonth(Date date) {
        return date.getDate() - 1;
    }

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

    public static String formatIntoCurrency(Context context, int currency, int price) {
        String numberFormat = NumberFormat.getIntegerInstance().format(price);
        String formatted = String.format(context.getString(R.string.money_format),
                currencyStringFromInt(context, currency, false), numberFormat);
        return formatted;
    }

    public static int dpToPixel(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pixelToDp( Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static List<DateTime> datesBetween(DateTime startDate, DateTime endDate, boolean includeEdge) {
        DateTime dateTime1 = startDate;
        DateTime dateTime2 = endDate;

        List<DateTime> dates = new ArrayList<>();

        if (includeEdge) { dates.add(startDate); }

        while(dateTime1.isBefore(dateTime2)){
            dates.add(dateTime1);
            dateTime1 = dateTime1.plusDays(1);
        }

        if (includeEdge) { dates.add(endDate); }

        return dates;
    }

    public static long[] dateArrayToLongArray(DateTime[] dates) {
        long[] longs = new long[dates.length];

        for (int i = 0; i < dates.length; ++i) {
            longs[i] = new DateTime(dates[i]).getMillis();
        }

        return longs;
    }

    public static DateTime[] longArrayToDateArray(long[] longs) {
        DateTime[] dates = new DateTime[longs.length];

        for (int i = 0; i < longs.length; ++i) {
            dates[i] = new DateTime(longs[i]);
        }

        return dates;
    }

    public static String formatPhoneNumber(String phoneNumber) {
        String phoneNumberString = PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().getCountry());
        return phoneNumberString;
    }

    public static String datetoString(Context context, DateTime datetime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(context.getString(R.string.date_format));
        String dateString = formatter.print(datetime);
        return dateString;
    }

    public static DateTime dateFromString(Context context, String dateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(context.getString(R.string.date_format));
        DateTime date = formatter.parseDateTime(dateString);
        return date;
    }
}
