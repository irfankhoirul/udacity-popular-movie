package com.irfankhoirul.popularmovie.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public class DateUtil {

    public static String formatDate(String dateInput, String inputFormat, String outputFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(inputFormat, Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(dateInput);
            SimpleDateFormat newDateFormat = new SimpleDateFormat(
                    outputFormat, Locale.getDefault());
            return newDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateInput;
    }

}
