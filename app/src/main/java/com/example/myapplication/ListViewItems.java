package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.CompareToKt;

public class ListViewItems extends AppCompatActivity {
    private Button startDate,startTime,endDate,endTime;
    private EditText eventName,description;
    private Switch switch1;
    EventsStore eventsStore;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_items);
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEvent();
            }
        });
        //Import from EventsActivity
        int id = getIntent().getIntExtra("id",0);
        String title = getIntent().getStringExtra("title");
        String End_date = getIntent().getStringExtra("End_date");
        String Start_time = getIntent().getStringExtra("Start_time");
        String End_time = getIntent().getStringExtra("End_time");
        int all_day = getIntent().getIntExtra("all_day",0);
        String remind_me = getIntent().getStringExtra("remind");
        String des = getIntent().getStringExtra("desc");
        String actual_start = getIntent().getStringExtra("actual_start");
        String prevContext = getIntent().getStringExtra("curContext");
//        String title = getIntent().getStringExtra("title");
        //Import xml components
        eventName = findViewById(R.id.eventName1);
        startDate = (Button)findViewById(R.id.startDate1);
        startTime = (Button)findViewById(R.id.startTime1);
        endDate = (Button)findViewById(R.id.endDate1);
        endTime = (Button)findViewById(R.id.endTime1);
        description = findViewById(R.id.description1);
        switch1 = findViewById(R.id.switch1);
        eventName.setText(title);
        startDate.setText(actual_start);
        startTime.setText(Start_time);
        endDate.setText(End_date);
        endTime.setText(End_time);
        Button update = findViewById(R.id.Update);
        Button delete = findViewById(R.id.delete);
        boolean variable = all_day==1?true:false;
        Spinner remindMe = (Spinner) findViewById(R.id.remindMe1);
        String items[] = {"None","On Time","5 Minutes Before","10 Minutes Before","15 Minutes Before","30 Minutes Before","1 hour Before","2 hours Before"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        remindMe.setAdapter(adapter);
        if(variable) {
            switch1.setChecked(true);
            items = new String[]{"None","On Day","1 day Before","2 days Before","1 week Before"};
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
            remindMe.setAdapter(adapter);
        }
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String items[];
                ArrayAdapter<String> adapter;
                if(isChecked){
                    startTime.setVisibility(View.INVISIBLE);
                    endTime.setVisibility(View.INVISIBLE);
                    items = new String[]{"None","On Day","1 day Before","2 days Before","1 week Before"};
                    adapter = new ArrayAdapter<String>(ListViewItems.this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
                    remindMe.setAdapter(adapter);
                }
                else{
                    items = new String[]{"None","On Time","5 Minutes Before","10 Minutes Before","30 Minutes Before","1 hour Before","2 hours Before"};
                    adapter = new ArrayAdapter<String>(ListViewItems.this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
                    remindMe.setAdapter(adapter);
                    startTime.setVisibility(View.VISIBLE);
                    endTime.setVisibility(View.VISIBLE);
                }
            }
        });
        int position = adapter.getPosition(remind_me); // get position of value
        remindMe.setSelection(position);
        description.setText(des);
        DBHandler db = new DBHandler(ListViewItems.this);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(startDate);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(endDate);
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(startTime);
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(endTime);
            }
        });
        //update function
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delete(title,remind_me,des,actual_start,End_date,Start_time,End_time,all_day,prevContext);
                AlarmSetting alarmSetting = new AlarmSetting();
                int id = 0;
                SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                boolean isInitialized = preferences.getBoolean("isInitialized", false);
                if (!isInitialized) {
                    String title = eventName.getText().toString();
                    String remind = remindMe.getSelectedItem().toString();
                    String desc = description.getText().toString();
                    int check = switch1.isChecked() ? 1 : 0;
                    if (!CompareTwoDates(startDate.getText().toString(), endDate.getText().toString()))
                        endDate.setText(startDate.getText().toString());
                    try {
                        if(startDate.getText().toString().equals(endDate.getText().toString())) {
                            eventsStore = new EventsStore(-1, title, remind, desc, check, startDate.getText().toString(), endDate.getText().toString(),startDate.getText().toString(), startTime.getText().toString(), endTime.getText().toString(),"ListViewItems.this");
                            boolean b = db.addOne(eventsStore);
                            id = eventsStore.getId();
                        }
                        else{
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                            Date start = sdf1.parse(startDate.getText().toString()),end = sdf1.parse(endDate.getText().toString());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(start);
                            while(calendar.getTime().before(end)||calendar.getTime().equals(end)){
//                                if(!sdf1.format(calendar.getTime()).equals(start))
//                                    startTime.setText("0:00");
                                String start_day = sdf1.format(calendar.getTime()),end_day = sdf1.format(end);
                                eventsStore = new EventsStore(-1,title,remind,desc,check,start_day,end_day,startDate.getText().toString(),startTime.getText().toString(),endTime.getText().toString(),"ListViewItems.this");
                                db.addOne(eventsStore);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        }
                        PostNotification(id,remind);
                        Toast.makeText(ListViewItems.this, "success", Toast.LENGTH_SHORT);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isInitialized", true);
                        editor.apply();
                    } catch (Exception e) {
                        Log.d(TAG, "onClick: "+e);
                        Toast.makeText(ListViewItems.this, "Error creating toast"+e, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    id = 0;
                    String title = eventName.getText().toString();
                    String remind = remindMe.getSelectedItem().toString();
                    String desc = description.getText().toString();
                    int check = switch1.isChecked() ? 1 : 0;
                    if (!CompareTwoDates(startDate.getText().toString(), endDate.getText().toString()))
                        endDate.setText(startDate.getText().toString());
                    try {
                        if(startDate.getText().toString().equals(endDate.getText().toString())) {
                            eventsStore = new EventsStore(-1, title, remind, desc, check, startDate.getText().toString(), endDate.getText().toString(),startDate.getText().toString(), startTime.getText().toString(), endTime.getText().toString(),"ListViewItems.this");
                            boolean b = db.addOne(eventsStore);
                            id = eventsStore.getId();
                        }
                        else{
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                            Date start = sdf1.parse(startDate.getText().toString()),end = sdf1.parse(endDate.getText().toString());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(start);
                            while(calendar.getTime().before(end)||calendar.getTime().equals(end)){
//                                if(!sdf1.format(calendar.getTime()).equals(start))
//                                    startTime.setText("0:00");
                                String start_day = sdf1.format(calendar.getTime()),end_day = sdf1.format(end);
                                eventsStore = new EventsStore(-1,title,remind,desc,check,start_day,end_day,startDate.getText().toString(),startTime.getText().toString(),endTime.getText().toString(),"ListViewItems.this");
                                db.addOne(eventsStore);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        }
                        PostNotification(id,remind);
                        Toast.makeText(ListViewItems.this, "success ", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(ListViewItems.this, MainActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.d(TAG, "onClick: "+e);
                        Toast.makeText(ListViewItems.this, "Error creating toast"+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //delete function
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delete(eventName.getText().toString(),remindMe.getSelectedItem().toString(),description.getText().toString(),startDate.getText().toString(),endDate.getText().toString(),startTime.getText().toString(),endTime.getText().toString(),all_day,prevContext);
                Intent intent = new Intent(ListViewItems.this,MainActivity.class);
                Toast.makeText(ListViewItems.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
    private void PostNotification(int id,String remind) {
        AlarmSetting alarmSetting = new AlarmSetting();
        String start[] = startDate.getText().toString().split("/"),time[] = startTime.getText().toString().split(":");
        int day = Integer.parseInt(start[0]),month = Integer.parseInt(start[1]),year = Integer.parseInt(start[2]),hour = Integer.parseInt(time[0]),min = Integer.parseInt(time[1]);
        if(!remind.equals("None")){
            if(remind.equals("5 Minutes Before")) {
                min = min - 5;
                if(min<0){
                    hour = hour - 1;
                    if(hour<0)
                        hour = 23;
                    min = 60 + min;
                }
            }
            else if(remind.equals("10 Minutes Before")){
                min = min - 10;
                if(min<0){
                    hour = hour - 1;
                    if(hour<0)
                        hour = 23;
                    min = 60 + min;
                }
            }
            else if(remind.equals("15 Minutes Before")){
                min = min - 15;
                if(min<0){
                    hour = hour - 1;
                    if(hour<0)
                        hour = 23;
                    min = 60 + min;
                }
            }
            else if(remind.equals("30 Minutes Before")){
                min = min - 30;
                if(min<0){
                    hour = hour - 1;
                    if(hour<0)
                        hour = 23;
                    min = 60 + min;
                }
            }
            else if(remind.equals("1 Hour Before")){
                hour = hour - 1;
                if(hour<0)
                    hour = 23;
            }
            else if(remind.equals("2 Hours Before")){
                hour = hour - 2;
                if(hour<0)
                    hour = 24+hour;
            }
            else if(remind.equals("1 day Before")){
                day = day - 1;
                if(day==0){
                    if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                        day = 31;
                    }
                    else if(month==4||month==6||month==9||month==11){
                        day = 30;
                    }
                    else
                        day = 28;
                    month = month - 1;
                    if(month==0){
                        month = 12;
                        year = year-1;
                    }
                }
            }
            else if(remind.equals("2 days Before")){
                day = day - 2;
                if(day==0){
                    if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                        day = 31+day;
                    }
                    else if(month==4||month==6||month==9||month==11){
                        day = 30+day;
                    }
                    else
                        day = 28+day;
                    month = month - 1;
                    if(month==0){
                        month = 12;
                        year = year-1;
                    }
                }
            }
            else if(remind.equals("1 week Before")){
                day = day - 7;
                if(day<=0){
                    if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                        day = 31+day;
                    }
                    else if(month==4||month==6||month==9||month==11){
                        day = 30+day;
                    }
                    else
                        day = 28+day;
                    month = month - 1;
                    if(month==0){
                        month = 12;
                        year = year-1;
                    }
                }
            }
            String title = eventName.getText().toString(),desc = description.getText().toString();
            alarmSetting.setAlarm(id,title,desc,day,month-1,year,hour,min,ListViewItems.this);
        }
    }
    public void openEvent() {
        Intent intent = new Intent(this,EventsActivity.class);
        startActivity(intent);
    }
    private void setTime(Button startTime) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ListViewItems.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if(minute>=10)
                            startTime.setText(hourOfDay + ":" + minute);
                        else
                            startTime.setText(hourOfDay+ ":0" + minute);
                    }
                }, hour, min, false);
        timePickerDialog.show();
    }

    private void setDate(Button date) {
        String s = "";
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR), month = c.get(Calendar.MONTH), day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ListViewItems.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date myDate = inFormat.parse(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
//                            String dayName = simpleDateFormat.format(myDate);
                            date.setText(inFormat.format(myDate)+"");
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                year,month,day);
        datePickerDialog.show();
    }

    private boolean CompareTwoDates(String Startdate, String Enddate) {
        String start[] = Startdate.split("/");
        String end[] = Enddate.split("/");
        int sY = Integer.parseInt(start[2]),sM = Integer.parseInt(start[1]), sD = Integer.parseInt(start[0]);
        int eY = Integer.parseInt(end[2]),eM = Integer.parseInt(end[1]), eD = Integer.parseInt(end[0]);
        if(sY>eY)
            return false;
        else if(sY==eY){
            if(sM>eM)
                return false;
            else if(sM==eM){
                if(sD>eD)
                    return false;
                else if(sD==eD)
                    return true;
            }
        }
        return true;
    }

}