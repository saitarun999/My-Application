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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Remainder extends AppCompatActivity {
    private Button startDate,startTime,endDate,endTime;
    private EditText eventName,description;
    int hour,min,year,month,day;
    private Switch switch1;
    Spinner remindMe;
    Calendar c;
    DBHandler db;
    EventsStore eventsStore;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEvent();
            }
        });
        Button save=findViewById(R.id.Save);
        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR),month = c.get(Calendar.MONTH),day = c.get(Calendar.DAY_OF_MONTH);
        eventName = findViewById(R.id.eventName);
        startDate = (Button)findViewById(R.id.startDate);
        startTime = (Button)findViewById(R.id.startTime);
        endDate = (Button)findViewById(R.id.endDate);
        endTime = (Button)findViewById(R.id.endTime);
        description = findViewById(R.id.description);
        switch1 = findViewById(R.id.switch1);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        remindMe = (Spinner) findViewById(R.id.remindMe);
//        Calendar calendar = Calendar.getInstance();
        Initializevalues(year,month,day);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String items[];
                ArrayAdapter<String> adapter;
                if(isChecked){
                    startTime.setVisibility(View.INVISIBLE);
                    endTime.setVisibility(View.INVISIBLE);
                    items = new String[]{"None","On Day","1 day Before","2 days Before","1 week Before"};
                    adapter = new ArrayAdapter<String>(Remainder.this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
                    remindMe.setAdapter(adapter);
                }
                else{
                    items = new String[]{"None","On Time","5 Minutes Before","10 Minutes Before","15 Minutes Before","30 Minutes Before","1 hour Before","2 hours Before"};
                    adapter = new ArrayAdapter<String>(Remainder.this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
                    remindMe.setAdapter(adapter);
                    startTime.setVisibility(View.VISIBLE);
                    endTime.setVisibility(View.VISIBLE);
                }
            }
        });
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
        //Start Date and End date
        String items[] = {"None","On Time","5 Minutes Before","10 Minutes Before","15 Minutes Before","30 Minutes Before","1 Hour Before","2 Hours Before"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        remindMe.setAdapter(adapter);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = 0;
                SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                boolean isInitialized = preferences.getBoolean("isInitialized", false);
                db = new DBHandler(Remainder.this);
                if (!isInitialized) {
                    String title = eventName.getText().toString();
                    String remind = remindMe.getSelectedItem().toString();
                    String desc = description.getText().toString();
                    int check = switch1.isChecked() ? 1 : 0;
                    if (!CompareTwoDates(startDate.getText().toString(), endDate.getText().toString()))
                        endDate.setText(startDate.getText().toString());
                    try {
                        if(startDate.getText().toString().equals(endDate.getText().toString())) {
                            eventsStore = new EventsStore(-1, title, remind, desc, check, startDate.getText().toString(),endDate.getText().toString(),startDate.getText().toString(), startTime.getText().toString(), endTime.getText().toString(),"Remainder.this");
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
                                eventsStore = new EventsStore(-1,title,remind,desc,check,start_day,end_day,startDate.getText().toString(),startTime.getText().toString(),endTime.getText().toString(),"Remainder.this");
                                db.addOne(eventsStore);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        }
                        PostNotification(id,remind);
                        Toast.makeText(Remainder.this, "success", Toast.LENGTH_SHORT);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isInitialized", true);
                        editor.apply();
                    } catch (Exception e) {
                        Log.d(TAG, "Luffy: "+e);
                        Toast.makeText(Remainder.this, "Error creating toast"+e, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String title = eventName.getText().toString();
                    String remind = remindMe.getSelectedItem().toString();
                    String desc = description.getText().toString();
                    int check = switch1.isChecked() ? 1 : 0;
                    if (!CompareTwoDates(startDate.getText().toString(), endDate.getText().toString()))
                        endDate.setText(startDate.getText().toString());
                    try {
                        if(startDate.getText().toString().equals(endDate.getText().toString())) {
                            eventsStore = new EventsStore(-1, title, remind, desc, check, startDate.getText().toString(), endDate.getText().toString(),startDate.getText().toString(), startTime.getText().toString(), endTime.getText().toString(),"Remainder.this");
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
                                eventsStore = new EventsStore(-1,title,remind,desc,check,start_day,end_day,startDate.getText().toString(),startTime.getText().toString(),endTime.getText().toString(),"Remainder.this");
                                db.addOne(eventsStore);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        }
                        PostNotification(id,remind);
                        Toast.makeText(Remainder.this, "success ", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(Remainder.this, MainActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.d(TAG, "Kaizoku "+e);
                        Toast.makeText(Remainder.this, "Error creating toast"+e, Toast.LENGTH_SHORT).show();
                    }
                }
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
            alarmSetting.setAlarm(id,title,desc,day,month-1,year,hour,min,Remainder.this);
        }
    }

    private void Initializevalues(int year,int month,int day) {
        SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date myDate = inFormat.parse(day+"/"+(month+1)+"/"+year);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
//            String dayName = simpleDateFormat.format(myDate);
            startDate.setText(inFormat.format(myDate)+"");
            endDate.setText(inFormat.format(myDate)+"");
            if(min>=10) {
                startTime.setText(hour + ":" + min);
                endTime.setText((hour+1)%24+ ":" + min);
            }
            else{
                startTime.setText(hour + ":0" + min);
                endTime.setText((hour+1)%24+ ":0" + min);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setTime(Button startTime) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(Remainder.this,
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
        final String[] s = {""};
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR), month = c.get(Calendar.MONTH), day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Remainder.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date myDate = inFormat.parse(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                             SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
//                            String dayName = simpleDateFormat.format(myDate);
                            date.setText(inFormat.format(myDate));
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                year,month,day);
        datePickerDialog.show();
    }
    private boolean CompareTwoDates(String Startdate,String Enddate) {
        String start[] = Startdate.split("/");
        String end[] = Enddate.split("/");
        int flag = 0;
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
            }
        }
        return true;
    }
    public void openEvent() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}