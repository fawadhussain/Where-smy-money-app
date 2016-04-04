package com.example.wsmm.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abubaker on 05/04/2016.
 */
public class SPManager {

    private static final String PREF_NAME = "prefName";
    private static final String USER_ID = "date";


    public static void setUserID(Context context, long mills) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(USER_ID, mills);
        editor.apply();
    }

    public static long getUserID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(USER_ID, 0);
    }

}
