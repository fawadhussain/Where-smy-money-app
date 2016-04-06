package com.example.wsmm.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abubaker on 05/04/2016.
 */
public class SPManager {

    private static final String PREF_NAME = "prefName";
    private static final String DATE_MILLI = "date";
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";


    public static void setDate(Context context, long mills) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(DATE_MILLI, mills);
        editor.apply();
    }

    public static long getDate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(DATE_MILLI, -1);
    }


    public static void setDay(Context context, int day) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(DAY, day);
        editor.apply();
    }

    public static int getDay(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(DAY, -1);
    }


    public static void setMonth(Context context, int month) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MONTH, month);
        editor.apply();
    }

    public static int getMonth(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(MONTH, -1);
    }



    public static void setYear(Context context, int year) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(YEAR, year);
        editor.apply();
    }

    public static int getYear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(YEAR, -1);
    }


    public static void clearPreferences(Context context){
        SharedPreferences settings = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        settings.edit().clear().apply();

    }

}
