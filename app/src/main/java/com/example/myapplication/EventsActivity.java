package com.example.myapplication;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EventsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    Button cancel,add;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Calendar calendar = Calendar.getInstance();
        cancel = findViewById(R.id.cancel);
        add = findViewById(R.id.add);
        ListView listView = findViewById(R.id.listView1);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        String date = getIntent().getStringExtra("date");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this,Remainder2.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
        DBHandler db = new DBHandler(EventsActivity.this);
        List<EventsStore> list = new ArrayList<>();
        try {
            list = db.getList(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int id1[] = new int[list.size()];
        String names[] = new String[list.size()];
        String Start_date[] = new String[list.size()];
        String End_date[] = new String[list.size()];
        String Actual_Start[] = new String[list.size()];
        String Start_time[] = new String[list.size()];
        String End_time[] = new String[list.size()];
        int all_day[] = new int[list.size()];
        String remind_me[] = new String[list.size()];
        String description[] = new String[list.size()];
        String curContext[] = new String[list.size()];
        int i = 0;
        for (EventsStore events : list) {
            id1[i] = events.getId();
            names[i] = events.getTitle();
            Start_date[i] = events.getStartDate();
            End_date[i] = events.getEndDate();
            Actual_Start[i] = events.getActualStartDate();
            Start_time[i] = events.getStartTime();
            End_time[i] = events.getEndTime();
            all_day[i] = events.isAllDay();
            remind_me[i] = events.getRemindMe();
            description[i] = events.getDescription();
            curContext[i] = events.getContext();
            i++;
        }
        EventListAdapter adapter = new EventListAdapter(EventsActivity.this, list);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(EventsActivity.this, ListViewItems.class);
                intent.putExtra("id", id1[position]);
                intent.putExtra("title", names[position]);
                intent.putExtra("End_date", End_date[position]);
                intent.putExtra("Start_time", Start_time[position]);
                intent.putExtra("End_time", End_time[position]);
                intent.putExtra("all_day", all_day[position]);
                intent.putExtra("remind", remind_me[position]);
                intent.putExtra("desc", description[position]);
                intent.putExtra("actual_start",Actual_Start[position]);
                intent.putExtra("curContext",curContext[position]);
                startActivity(intent);
            }
        });
    }
}

