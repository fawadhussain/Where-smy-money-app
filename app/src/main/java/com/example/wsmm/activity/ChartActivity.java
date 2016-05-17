package com.example.wsmm.activity;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.wsmm.R;
import com.example.wsmm.adapter.CategoryDialogAdapter;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.fragment.LineChartFragment;
import com.example.wsmm.fragment.PieFragment;
import com.example.wsmm.model.Category;
import com.example.wsmm.model.CategoryItem;
import com.example.wsmm.util.GeneralUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by abubaker on 14/05/2016.
 */
public class ChartActivity extends BaseActivity implements View.OnClickListener, OnDateSelectedListener ,DatePickerDialog.OnDateSetListener{


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
    Dialog categoryDialog = null;
    private List<Category> categories;

    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        addFragment(new PieFragment(), false, "LineChart");
        leftButton = (Button) findViewById(R.id.left_button);
        rightButton = (Button) findViewById(R.id.right_button);
        selectDate = (Button) findViewById(R.id.select_data);
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

                        switch (item.getItemId()) {
                            case R.id.item_day:
                                week = false;
                                month = true;
                                return true;
                            case R.id.item_week:
                                week = true;
                                month = false;
                                return true;
                            case R.id.item_seven_days:

                                LineChartFragment fragment = new LineChartFragment();
                                db = new DBClient();
                                List<Category> list = new ArrayList<Category>();
                                list = db.getLastSevenDaysData();
                                fragment.setRealmList(list);
                                try {
                                    if (adapter!= null && adapter.getSelectedMap()!= null)
                                    fragment.setSelectCategories(adapter.getSelectedMap());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                replaceFragment(fragment, true, false, "LineChartFragment");


                                return true;

                            case R.id.item_thirty_days:

                                LineChartFragment fragment1 = new LineChartFragment();
                                db = new DBClient();
                                List<Category> thirty = new ArrayList<Category>();
                                thirty = db.getLastMonthDaysData();


                                fragment1.setRealmList(thirty);
                                try {
                                    if (adapter!= null && adapter.getSelectedMap()!= null)
                                        fragment1.setSelectCategories(adapter.getSelectedMap());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                replaceFragment(fragment1, true, false, "LineChartFragment");

                                return true;


                            case R.id.item_custom_range:

                                Calendar now = Calendar.getInstance();
                                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                                        ChartActivity.this,
                                        now.get(Calendar.YEAR),
                                        now.get(Calendar.MONTH),
                                        now.get(Calendar.DAY_OF_MONTH)
                                );
                                dpd.show(getFragmentManager(), "Range Picker");
                                return true;


                        }
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


    private void selectCategoryDialog() {
        categoryDialog = new Dialog(ChartActivity.this);
        categoryDialog.setContentView(R.layout.category_dialog_layout);
        categoryDialog.setTitle("Category Dialog");
        final ImageView selectAll = (ImageView) categoryDialog.findViewById(R.id.select_icon);
        final RelativeLayout relativeLayout = (RelativeLayout) categoryDialog.findViewById(R.id.select_all_category_layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check) {
                    selectAll.setImageResource(R.drawable.checkbox_hollow_icon);
                    adapter.clearAll();
                    check = false;

                } else {
                    selectAll.setImageResource(R.drawable.ticked_checkbox_icon);
                    adapter.selectAllCategories();
                    check = true;

                }

            }
        });

        mRecyclerView = (RecyclerView) categoryDialog.findViewById(R.id.category_list_selection);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = new DBClient();
        itemList = db.getCategoryList();
        adapter = new CategoryDialogAdapter(this, itemList);
        mRecyclerView.setAdapter(adapter);


        categoryDialog.show();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button:
                if (categoryDialog != null){
                    categoryDialog.show();

                }else {
                    selectCategoryDialog();

                }

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
        RealmResults<Category> results = db.getParticularRealmResult(date.getDay(), date.getMonth() + 1, date.getYear());
        PieFragment pieFragment = new PieFragment();
        pieFragment.setRealmDataSet(results);

        replaceFragment(pieFragment, true, false, "PieFragment");


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


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        LineChartFragment lineChartFragment = new LineChartFragment();

        db = new DBClient();
        categories = db.getCustomDateData(GeneralUtils.getTimeInMillis(dayOfMonth - 1, monthOfYear, year)
                , GeneralUtils.getTimeInMillis(dayOfMonthEnd + 1, monthOfYearEnd, yearEnd));

        lineChartFragment.setRealmList(categories);
        try {
            if (adapter!= null && adapter.getSelectedMap()!= null)
                lineChartFragment.setSelectCategories(adapter.getSelectedMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        replaceFragment(lineChartFragment, true, false, "LineChartFragment");



    }
}
