package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    int currentYear = 0, currentMonth = 0, currentDay = 0;
    Calendar c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = Calendar.getInstance();
        CalendarView calendarView = findViewById(R.id.calendarView);
        Button todayButton = findViewById(R.id.todayButton);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setDate(calendarView.getDate());
            }
        });
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEvent();
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfChange) {
                try {
                    openEvent1(year,month,dayOfChange);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openEvent1(int year,int month,int dayOfChange) throws ParseException {
        Intent intent = new Intent(this,EventsActivity.class);
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        Date myDate = sd.parse(dayOfChange+"/"+(month+1)+"/"+year);
        SimpleDateFormat sd1 = new SimpleDateFormat("EEE");
        String dayName = sd1.format(myDate);
        String date = sd.format(myDate)+"";
        Log.d(TAG, "openEvent1: "+date);
        intent.putExtra("date",date);
        startActivity(intent);
    }

    public void openEvent(){
        Intent intent = new Intent(this,Remainder.class);
        startActivity(intent);
    }


}