package com.example.wsmm.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abubaker on 05/04/2016.
 */
public class SPManager {

    private static final String PREF_NAME = "wsmm";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String ALARM = "alarm";
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


    public static void setDate(Context context, String date) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DATE,date);
        editor.apply();;
    }


    public static String getDate(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(DATE,null);
    }


    public static void setTime(Context context, String time) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TIME,time);
        editor.apply();
    }


    public static String getTime(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(TIME,null);
    }


    public static void setAlarm(Context context, boolean alarm){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ALARM,alarm);
        editor.apply();

    }

    public static boolean checkAlarm(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(ALARM,false);
    }

}
