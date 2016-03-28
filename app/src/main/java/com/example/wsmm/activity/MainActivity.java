package com.example.wsmm.activity;



import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsmm.fragment.AddTransactionFragment;
import com.example.wsmm.fragment.PrimaryFragment;
import com.example.wsmm.R;
import com.example.wsmm.TabFragment;
import com.example.wsmm.fragment.NavigationDrawerFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;


public class MainActivity extends BaseActivity implements View.OnClickListener, PrimaryFragment.OnUpdateToolBar ,PopupMenu.OnMenuItemClickListener, OnDateSelectedListener {


    Calendar cal;
    TextView datePickerText;
    MaterialCalendarView widget;
    private boolean week = false;
    private boolean month = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // widget = (MaterialCalendarView) parent.findViewById(R.id.calendarView);

       // dateText = (TextView) findViewById(R.id.date_picker);
        setSupportActionBar(mToolbar);
        datePickerText = (TextView) findViewById(R.id.date_picker_text);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cal = Calendar.getInstance();
        datePickerText.setText("TODAY " + getMonth(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.DAY_OF_MONTH));


        datePickerText.setOnClickListener(this);
       // findViewById(R.id.period_picker).setOnClickListener(this);


        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        addFragment(new PrimaryFragment(), false,"AddTransaction");

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
      //  findViewById(R.id.date_picker).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

       switch (v.getId()) {
            case R.id.date_picker_text:

                datePickerDialog();

             /*   new DatePickerDialog(MainActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        datePickerText.setText(year + "," + getMonth(monthOfYear) + " " + dayOfMonth);
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.DAY_OF_MONTH,monthOfYear);
                        c.set(Calendar.YEAR,year);
                        c.set(Calendar.MONTH, monthOfYear);

                        long mills = c.getTimeInMillis();

                        FragmentManager fm = getSupportFragmentManager();


                            if (fm.findFragmentByTag("AddTransaction").isVisible()){
                                AddTransactionFragment fragment = (AddTransactionFragment)fm.findFragmentByTag("AddTransaction");
                                fragment.setChangeDate(mills);

                            }



                        //DO SOMETHING
                    }
                },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();*/
                break;



        }

    }


    @Override
    public void onUpdatePrice(String price) {

        //  Toast.makeText(getApplicationContext(),price,Toast.LENGTH_SHORT).show();

    }






    private String getMonth(int i) {

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


    private String getDay(int day){
        switch (day){


        }
        return "";
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_day:
                week = false;
                month = true;
                return true;
            case R.id.item_week:
               // Toast.makeText(this, "Week", Toast.LENGTH_SHORT).show();
                week = true;
                month = false;
                //widget.setCalendarDisplayMode(CalendarMode.WEEKS);
                return true;
            case R.id.item_seven_days:
                Toast.makeText(this, "Seven", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_thirty_days:
                Toast.makeText(this, "Thirty", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_custom_range:
                Toast.makeText(this, "Custom", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.day_range:
                View dayRange = findViewById(R.id.day_range);
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, dayRange);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void datePickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_picker_dialog_fragment);
        widget = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        Calendar mDataCalendar = Calendar.getInstance();
        widget.setSelectedDate(mDataCalendar);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        if (week){
            widget.setCalendarDisplayMode(CalendarMode.WEEKS);
        }else {
            widget.setCalendarDisplayMode(CalendarMode.MONTHS);
        }

        //widget = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
       // widget.setOnMonthChangedListener(this);
        // widget.addDecorators(mEventDecorator);
    /*    Button btn_yes = (Button) dialog.findViewById(R.id.bt_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.bt_no);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });*/

        dialog.show();

    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay day, boolean selected) {

       /* for (int i = 0; i < mSelectedDates.size(); i++) {
            TSHCalendarEvent event = mSelectedDates.get(i);
            Calendar mDate = DateTimeUtils.calendarDateFormat(event.getStartDate());
            Calendar mDateEnd = null;

            if (!event.isAllday()) {
                mDateEnd = DateTimeUtils.calendarDateFormat(event.getEndDate());
            }


            if (day.getYear() == mDate.get(Calendar.YEAR) && day.getMonth() == mDate.get(Calendar.MONTH) && day.getDay() == mDate.get(Calendar.DAY_OF_MONTH)) {

                tshCalendarEvents.add(event);

            }
            else if(mDateEnd!=null){
                if (day.getYear() == mDateEnd.get(Calendar.YEAR) && day.getMonth() == mDateEnd.get(Calendar.MONTH) && day.getDay() == mDateEnd.get(Calendar.DAY_OF_MONTH)) {
                    tshCalendarEvents.add(event);
                }
            }

        }

        if (tshCalendarEvents.size() > 0) {
            widget.setCalendarDisplayMode(CalendarMode.WEEKS);
            eventsList.setVisibility(View.VISIBLE);
            eventsAdapter.clear();
            eventsAdapter.addAll(tshCalendarEvents);
            oneDayDecorator.setDate(day.getDate());
            widget.invalidateDecorators();
        }*/


    }
}
