package com.example.wsmm.fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wsmm.R;
import com.example.wsmm.util.AlarmReceiver;
import com.example.wsmm.util.SPManager;

import java.util.Calendar;

/**
 * Created by abubaker on 5/24/16.
 */
public class AlarmFragment extends BaseFragment implements View.OnClickListener{


    Button Date, Time;
    EditText etTime, etDate;
    TimePickerFragment tpf = new TimePickerFragment();
    DatePickerFragment dpf = new DatePickerFragment();
    Button save;
    Button cancel;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public int getLayoutId() {
        return R.layout.alarm_screen_layout;

    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
        setHasOptionsMenu(true);
        Time = (Button) parent.findViewById(R.id.time_picker);
        Date = (Button) parent.findViewById(R.id.date_picker);
        etTime = (EditText) parent.findViewById(R.id.ettime);
        etDate = (EditText) parent.findViewById(R.id.etdate);
        save = (Button) parent.findViewById(R.id.save);
        cancel=(Button) parent.findViewById(R.id.cancel);
        Time.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        Date.setOnClickListener(this);


        etTime.setClickable(false);
        etTime.setFocusable(false);
        etDate.setFocusable(false);
        etDate.setClickable(false);

        if (SPManager.checkAlarm(getActivity())){
            etDate.setText(SPManager.getDate(getActivity()));
            etTime.setText(SPManager.getTime(getActivity()));
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);

        }else {
            save.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_picker:
                tpf.showTimePickerDialog();
                break;
            case R.id.date_picker:
                dpf.showDatePickerDialog();
                break;
            case R.id.save:
                SetAlarm();
                break;
            case R.id.cancel:
                cancelAlarm();
                break;
        }
    }


    @SuppressLint("ValidFragment")
    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            etTime.setText(hourOfDay+":"+minute);
            SPManager.setTime(getActivity(),hourOfDay+":"+minute);
        }

        public void showTimePickerDialog() {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(AlarmFragment.this.getFragmentManager(), "timePicker");
        }
    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            int update=month+1;
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            etDate.setText(year+"-"+"0"+update+"-"+day);
            save.setVisibility(View.VISIBLE);
            SPManager.setDate(getActivity(),year+"-"+"0"+update+"-"+day);

        }

        public void showDatePickerDialog() {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(AlarmFragment.this.getFragmentManager(), "datePicker");
        }
    }



    public void SetAlarm() {
        alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        Toast.makeText(getActivity(),"Alarm is Set", Toast.LENGTH_SHORT).show();
        cancel.setVisibility(View.VISIBLE);
        SPManager.setAlarm(getActivity(),true);

    }

    private void cancelAlarm() {
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
            Toast.makeText(getActivity(),"Alarm is Canceled", Toast.LENGTH_SHORT).show();
        }

        if (alarmIntent != null){
            alarmIntent.cancel();
        }

        SPManager.setDate(getActivity(),null);
        SPManager.setTime(getActivity(),null);
        SPManager.setAlarm(getActivity(),false);
        etDate.setText("");
        etTime.setText("");
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.add_transction, menu);

    }
}
