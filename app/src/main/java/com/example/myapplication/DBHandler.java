package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.metrics.Event;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "eventHandler";
    public static final String EVENT_ID = "eventid";
    public static final String NAME_COL = "eName";
    public static final String START_DATE = "eStart_Date";
    public static final String END_DATE = "eEnd_Date";
    public static final String ACTUAL_START_DATE = "eActual_Start_Date";
    public static final String START_TIME = "eStart_Time";
    public static final String END_TIME = "eEnd_Time";
    public static final String allDay = "eAll_Day";
    public static final String remindMe = "eRemind_Me";
    public static final String description = "eDescription";
    public static final String context = "eContext";
    static String DB_NAME = "Schedule";

    // below int is our database version
    private static int DB_VERSION = 1;

    // below variable is for our table name.

    // below variable for our course description column.
//    private static String DESCRIPTION_COL = "description";
    public DBHandler(Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String query = " CREATE TABLE " + TABLE_NAME + " ( " + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COL + " TEXT, " + START_DATE + " TEXT, " + END_DATE + " TEXT, " + ACTUAL_START_DATE + " TEXT, " + START_TIME + "  TEXT, " + END_TIME + " TEXT, " + allDay + " INTEGER, " + remindMe + " TEXT, " + description + " TEXT, "+context+" TEXT ) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean addOne(EventsStore eventsStore){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME_COL,eventsStore.getTitle());
        cv.put(START_DATE,eventsStore.getStartDate());
        cv.put(END_DATE,eventsStore.getEndDate());
        cv.put(START_TIME,eventsStore.getStartTime());
        cv.put(END_TIME,eventsStore.getEndTime());
        cv.put(ACTUAL_START_DATE,eventsStore.getActualStartDate());
        cv.put(allDay,eventsStore.isAllDay());
        cv.put(remindMe,eventsStore.getRemindMe());
        cv.put(description,eventsStore.getDescription());
        cv.put(context,eventsStore.getContext());
        ;
        long v = db.insert(TABLE_NAME, null, cv);
        if(v==-1){
            return false;
        }
        return true;
    }
    public List<EventsStore> getList(String date) throws ParseException {
        List<EventsStore> eventsStores = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+START_DATE+" = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{date});
        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(EVENT_ID));
                @SuppressLint("Range") String eventName = cursor.getString(cursor.getColumnIndex(NAME_COL));
                @SuppressLint("Range") String start_date = cursor.getString(cursor.getColumnIndex(START_DATE));
                @SuppressLint("Range") String end_date = cursor.getString(cursor.getColumnIndex(END_DATE));
                @SuppressLint("Range") String start_time = cursor.getString(cursor.getColumnIndex(START_TIME));
                @SuppressLint("Range") String end_time = cursor.getString(cursor.getColumnIndex(END_TIME));
                @SuppressLint("Range") int all_day = cursor.getInt(cursor.getColumnIndex(allDay));
                @SuppressLint("Range") String remind_me = cursor.getString(cursor.getColumnIndex(remindMe));
                @SuppressLint("Range") String Description = cursor.getString(cursor.getColumnIndex(description));
                @SuppressLint("Range") String actual_start = cursor.getString(cursor.getColumnIndex(ACTUAL_START_DATE));
                @SuppressLint("Range") String Context = cursor.getString(cursor.getColumnIndex(context));
                EventsStore events = new EventsStore(id,eventName,remind_me,Description,all_day,start_date,end_date,actual_start,start_time,end_time,Context);
                eventsStores.add(events);
                Log.d(TAG, "getList: "+events.toString());
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventsStores;
    }
    public boolean Update(EventsStore eventsStore){
        int id = eventsStore.getId();
//        String query = "UPDATE "+TABLE_NAME+" SET "+NAME_COL+" = "+eventsStore.getTitle()+" , "+START_DATE+" = "+eventsStore.getStartDate()+" , "+END_DATE+" = "+eventsStore.getEndDate()+" , "+START_TIME+" = "+eventsStore.getStartTime()+" , "+END_TIME+" = "+eventsStore.getEndTime()+" , "+allDay+" = "+eventsStore.isAllDay()+" , "+remindMe+" = "+eventsStore.getRemindMe()+" , "+description+" = "+eventsStore.getDescription()+" WHERE "+EVENT_ID+" = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME_COL,eventsStore.getTitle());
        cv.put(START_DATE,eventsStore.getStartDate());
        cv.put(END_DATE,eventsStore.getEndDate());
        cv.put(ACTUAL_START_DATE,eventsStore.getActualStartDate());
        cv.put(START_TIME,eventsStore.getStartTime());
        cv.put(END_TIME,eventsStore.getEndTime());
        cv.put(allDay,eventsStore.isAllDay());
        cv.put(remindMe,eventsStore.getRemindMe());
        cv.put(description,eventsStore.getDescription());
        cv.put(context,eventsStore.getContext());
        db.update(TABLE_NAME,cv,"eventid = ?",new String[]{String.valueOf(id)});
        db.close();
//        db.execSQL(query);
        return true;
    }
    public void delete(String title, String remind, String desc, String startDate, String endDate, String startTime, String endTime,int all_day,String curcontext) {
//        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+NAME_COL+" = "+title+" AND "+remindMe+" = "+remind+" AND "+description+" = "+desc+" AND "+ACTUAL_START_DATE+" = "+startDate+" AND "+END_DATE+" = "+endDate+" AND "+START_TIME+" = "+startTime+" AND "+END_TIME+" = "+endTime+" AND "+context+" = "+curcontext;
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = NAME_COL+ " = ? AND " + remindMe + " = ? AND " + description + " = ? AND " + ACTUAL_START_DATE + " = ? AND " + END_DATE + " = ? AND " + START_TIME + " = ? AND " + END_TIME + " = ? AND " + allDay +" = ? AND " + context + " = ?";
        Log.d(TAG, "delete: "+title+" "+remind+" "+desc+" "+startDate+" "+endDate+" "+startTime+" "+endTime+" "+String.valueOf(all_day)+" "+curcontext);
        String whereargs[] = new String[]{title,remind,desc,startDate,endDate,startTime,endTime,String.valueOf(all_day),curcontext};
        int deleteRows = db.delete(TABLE_NAME,whereClause,whereargs);
        db.close();
    }
}
