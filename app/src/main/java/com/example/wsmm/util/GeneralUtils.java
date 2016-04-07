package com.example.wsmm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by abubaker on 08/04/2016.
 */
public class GeneralUtils {

   private static Calendar calendar =Calendar.getInstance();

    public static int getDay(long date){


        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.DAY_OF_MONTH);

    }


    public static int getMonth(long date){

        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.MONTH);

    }


    public static int getYear(long date){

        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.YEAR);

    }


    public static long getCurrentSystemDate() {
        return System.currentTimeMillis();
    }

    public static String getFormattedDateString(long mills){

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDate.format(mills);

    }

    public static String getCurrentFormattedDate(){

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDate.format(getCurrentSystemDate());

    }




}
