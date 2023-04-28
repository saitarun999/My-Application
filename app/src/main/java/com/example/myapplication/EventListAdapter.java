package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
//how the contents must look in listView
public class EventListAdapter extends ArrayAdapter<EventsStore> {
    Context context;
    List<EventsStore> list;
    public EventListAdapter(@NonNull Context context, List<EventsStore> list) {
        super(context,R.layout.list_item_event,list);
        this.context = context;
        this.list = list;
    }
    public View getView(int position,View contentView,ViewGroup parent){
        View view = contentView;
        if(view==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_item_event,parent,false);
        }
        EventsStore eventsStore = list.get(position);
        TextView title = view.findViewById(R.id.title_name);
        TextView start = view.findViewById(R.id.start_end);
        title.setText(eventsStore.getTitle());
        start.setText(eventsStore.getActualStartDate()+","+eventsStore.getStartTime()+" to "+eventsStore.getEndDate()+","+eventsStore.getEndTime());
        return view;
    }
}
