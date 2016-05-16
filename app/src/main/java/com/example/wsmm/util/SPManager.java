package com.example.wsmm.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abubaker on 05/04/2016.
 */
public class SPManager {

    private static final String PREF_NAME = "wsmm";
    private static final String DATE_MILLI = "date";
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private static final String CURRENCY = "currency";




    public static void setCurrency(Context context, int currency) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CURRENCY, currency);
        editor.apply();
    }

    public static int getCurrency(Context context) {

      SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(CURRENCY,-1);
    }

}
