package com.example.wsmm.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.wsmm.R;
import com.example.wsmm.adapter.CategoryDialogAdapter;
import com.example.wsmm.adapter.CurrencyAdapter;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.fragment.LineChartFragment;
import com.example.wsmm.fragment.PieFragment;
import com.example.wsmm.model.Category;
import com.example.wsmm.model.CategoryItem;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by abubaker on 14/05/2016.
 */
public class ChartActivity extends BaseActivity implements View.OnClickListener , OnDateSelectedListener {


    private RecyclerView mRecyclerView;
    List<String> categoryList;
    private CategoryDialogAdapter adapter;
    Button leftButton;
    Button rightButton;
    Button selectDate;
    private boolean week = false;
    private boolean month = false;
    DBClient db;
    MaterialCalendarView widget;
    private Dialog dialog;
    private List<CategoryItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        addFragment(new PieFragment(),false,"LineChart");
        leftButton = (Button)findViewById(R.id.left_button);
        rightButton = (Button)findViewById(R.id.right_button);
        selectDate = (Button)findViewById(R.id.select_data);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ChartActivity.this, leftButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
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

                              LineChartFragment fragment = new LineChartFragment();
                                db = new DBClient();
                                List<Category> list = new ArrayList<Category>();
                                list = db.getLastSevenDaysData();
                                fragment.setRealmList(list);
                                replaceFragment(fragment,true,false,"LineChartFragment");


                                return true;

                            case R.id.item_thirty_days:

                                LineChartFragment fragment1 = new LineChartFragment();
                                db = new DBClient();
                                List<Category> thirty = new ArrayList<Category>();
                                thirty = db.getLastMonthDaysData();
                                fragment1.setRealmList(thirty);
                                replaceFragment(fragment1,true,false,"LineChartFragment");



                        }

                        Toast.makeText(
                                ChartActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }


        });
        rightButton.setOnClickListener(this);
        selectDate.setOnClickListener(this);



    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
         if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            finish();
        }
    }


    private void selectCategoryDialog(){
        final Dialog dialog = new Dialog(ChartActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.category_dialog_layout);
        // Set dialog title
        dialog.setTitle("Category Dialog");

        mRecyclerView = (RecyclerView) dialog.findViewById(R.id.category_list_selection);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = new DBClient();
        itemList = db.getCategoryList();
       // categoryList = Arrays.asList(getResources().getStringArray(R.array.categories));
        adapter = new CategoryDialogAdapter(this, itemList);
        mRecyclerView.setAdapter(adapter);



        dialog.show();



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_button:
                selectCategoryDialog();

                break;

            case R.id.select_data:

                datePickerDialog();


                break;


        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        dialog.dismiss();
        db = new DBClient();
        RealmResults<Category> results = db.getParticularRealmResult(date.getDay(),date.getMonth()+1,date.getYear());
        PieFragment pieFragment = new PieFragment();
        pieFragment.setRealmDataSet(results);

        replaceFragment(pieFragment,true,false,"PieFragment");








    }


    private void datePickerDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_picker_dialog_fragment);
        widget = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        Calendar mDataCalendar = Calendar.getInstance();
        widget.setSelectedDate(mDataCalendar);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        if (week) {
            widget.setCalendarDisplayMode(CalendarMode.WEEKS);
        } else {
            widget.setCalendarDisplayMode(CalendarMode.MONTHS);
        }

        dialog.show();

    }
}
