package edu.kennesaw.group4.totm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static android.content.Context.MODE_PRIVATE;

public class EventManager {
    //private Context context;
    static private ArrayList<Event> events;
    static Lock l = new ReentrantLock();
    /*
    EventManager(Context context) {
        //this.context = context;
        LoadEvents(context);
    }*/

    static void AddEvent(Event event, Context context) {
        LoadEvents(context);
        events.add(event);
        TOTMPole.scheduleNext48(context); //need to do this again because we've edited the active events
        SaveEvents(context);
    }

    static ArrayList<Event> getEvents(Calendar day, Context context) {
        if (events == null)
            LoadEvents(context);
        ArrayList<Event> result = new ArrayList<>();
        int dayofYear = day.get(Calendar.DAY_OF_YEAR);
        int year = day.get(Calendar.YEAR);
        for (Event event : events) {
            int eDayofYear = event.dateTime.get(Calendar.DAY_OF_YEAR);
            int eYear = event.dateTime.get(Calendar.YEAR);
            if (eDayofYear == dayofYear && eYear == year) {
                result.add(event);
            }
        }
        return result;
    }

    static public void DeleteEvent(Context context, String eventUUID) {
        LoadEvents(context);
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if (event.uuid.equals(eventUUID)) {
                events.remove(i);
                break;
            }
        }

        //events.remove(event);
        SaveEvents(context);
    }

    static void deleteAllEvents(Context context) {
        LoadEvents(context);
        events.clear();
        SaveEvents(context);
    }

    static private void LoadEvents(Context context) {
        events = new ArrayList<>();
        events.clear();
        SharedPreferences sp = context.getSharedPreferences("sp", MODE_PRIVATE);
        String eventsList = sp.getString("events", "[]");
        try {
            JSONArray jsonArray = new JSONArray(eventsList);
            for (int i = 0; i < jsonArray.length(); i++) {
                String eventString = jsonArray.getString(i);
                Event newEvent = Event.parseJSON(eventString);
                events.add(newEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static private void SaveEvents(Context context) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            try {
                jsonArray.put(event.toJSON());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String eventsList = jsonArray.toString();
        SharedPreferences sp = context.getSharedPreferences("sp", MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("events", eventsList);
        editor.apply();
    }
}
