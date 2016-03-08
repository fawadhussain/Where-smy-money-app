package com.example.wsmm.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsmm.fragment.PrimaryFragment;
import com.example.wsmm.R;
import com.example.wsmm.TabFragment;
import com.example.wsmm.fragment.NavigationDrawerFragment;

import java.util.Calendar;


public class MainActivity extends BaseActivity implements View.OnClickListener, PrimaryFragment.OnUpdateToolBar ,PopupMenu.OnMenuItemClickListener {


    Calendar cal;
    private Spinner spinner;
    TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        dateText = (TextView) findViewById(R.id.date_picker);
        setSupportActionBar(mToolbar);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cal = Calendar.getInstance();
        dateText.setText("TODAY " + getMonth(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.DAY_OF_MONTH));


        findViewById(R.id.date_picker).setOnClickListener(this);
        findViewById(R.id.period_picker).setOnClickListener(this);


        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        addFragment(new PrimaryFragment(), false);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        findViewById(R.id.date_picker).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.date_picker:

                new DatePickerDialog(MainActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateText.setText(year+","+getMonth(monthOfYear)+" "+ dayOfMonth);



                        //DO SOMETHING
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.period_picker:

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();


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
                Toast.makeText(this, "Day", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_week:
                Toast.makeText(this, "Week", Toast.LENGTH_SHORT).show();
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
}
