package com.example.wsmm.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.example.wsmm.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by abubaker on 08/04/2016.
 */
public class GeneralUtils {

    private static Uri uri = null;
    static List<String> currencyList;

    private static Calendar calendar = Calendar.getInstance();

    public static int getDay(long date) {


        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.DAY_OF_MONTH);

    }


    public static int getMonth(long date) {

        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.MONTH);

    }


    public static int getYear(long date) {

        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.YEAR);

    }


    public static long getCurrentSystemDate() {
        return System.currentTimeMillis();
    }

    public static String getFormattedDateString(long mills) {

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDate.format(mills);

    }

    public static String getCurrentFormattedDate() {

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDate.format(getCurrentSystemDate());

    }


    public static File createCSVFile(Context context) throws IOException {

        // Create an csv file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String imageFileName = "CSV_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File appFolder = new File(storageDir, context.getResources().getString(R.string.app_name));
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        File csvFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".csv",         /* suffix */
                appFolder      /* directory */
        );

        uri = Uri.fromFile(csvFile);


        // Save a file: path for use with ACTION_VIEW intents
        return csvFile;
    }


    public static Uri getUri() {
        return uri;
    }


    public static long getPreviousDate(int days) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return cal.getTimeInMillis();

    }


    public static long getTimeInMillis(int day, int month, int year) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis();

    }

    public static String getCurrencySymbol(Context context,int position){
        currencyList = Arrays.asList(context.getResources().getStringArray(R.array.currency_symbol));
        return currencyList.get(position);
    }


    public String getMonthString(int i) {

        switch (i) {
            case 1:
                return "FEB";

            case 2:
                return "MAR";

            case 3:
                return "APR";

            case 4:
                return "MAY";

            case 5:
                return "JUN";

            case 6:
                return "JUL";

            case 7:
                return "AUG";

            case 8:
                return "SEP";

            case 9:
                return "OCT";

            case 10:
                return "NOV";
            case 11:
                return "DEC";
            default:
                return "JAN";

        }


    }


}
