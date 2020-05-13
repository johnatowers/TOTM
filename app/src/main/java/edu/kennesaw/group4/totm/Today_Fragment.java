package edu.kennesaw.group4.totm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;


public class Today_Fragment extends Fragment {

    TextView dateTitle;

    TableLayout schoolTable;
    TableLayout errandsTable;
    TableLayout phoneTable;
    TableLayout otherTable;
    TableLayout completedTable;

    //ArrayList<Event> storedEvents=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        // TODO: These are the handles to the views.
        dateTitle = view.findViewById(R.id.title_textview);
        schoolTable = view.findViewById(R.id.school_table);
        errandsTable = view.findViewById(R.id.errands_table);
        phoneTable = view.findViewById(R.id.phone_calls_table);
        otherTable = view.findViewById(R.id.other_table);
        completedTable = view.findViewById(R.id.completed_table);

        dateTitle.setText(setDate());
        getActivity().setTitle("TOTM                                                               Today's Tasks");

        // TODO: These functions clear all previous rows (views) inserted into the TableViews.
        schoolTable.removeAllViews();
        errandsTable.removeAllViews();
        phoneTable.removeAllViews();
        otherTable.removeAllViews();
        completedTable.removeAllViews();

        // TODO: this is the ArrayList that is directly called form the event manager; "master list"
        ArrayList<Event> todaysEvents = EventManager.getEvents(Calendar.getInstance(), getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            todaysEvents.sort(new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return o1.dateTime.compareTo(o2.dateTime);
                }
            });
        }


        for (Event event : todaysEvents) {
            makeTagGUI(event.uuid, event.name, event.dateTime, event.address, event.category, event.checked, event.notification,event.alarm, view, savedInstanceState);
        }

        /*
        // TODO: the array that will be a copy of the original
        // TODO: the original array is copied to the new array
        // TODO: this is also the array that is used to populate the TableViews now.
        ArrayList<Event> storedEvents = new ArrayList<>(todaysEvents);

        // TODO: The list in now populated by the 'copy' array
        for (Event event:storedEvents) {

            // TODO: Index of current event in array is saved to variable
            String index = Integer.toString(storedEvents.indexOf(event));


            String name = event.name;
            String dateTime = MainScreen.CalendarToString(event.dateTime);
            String where = event.address;
            String category = event.category;

            // TODO: Copy of the category to be used as a reference.
            String categoryStorage = event.category;
            makeTagGUI(index, name, dateTime, where, category, categoryStorage, storedEvents);

        }*/

    }

    // TODO: This function creates the TableRows (events) that are input into the TableViews.
    private void makeTagGUI(String uuid, String name, Calendar dateTime, String where, String category, boolean checked, Calendar notification, Calendar alarm, View view, Bundle savedState) {//, String categorySaved, ArrayList<Event> storedEvents) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.new_task_view_today, null);
        TextView nameText = row.findViewById(R.id.new_task_name_textview);
        nameText.setText(name);
        TextView dayText = row.findViewById(R.id.new_task_day_textview);
        dayText.setText(MainScreen.CalendarToString(dateTime));
        TextView timeText = row.findViewById(R.id.new_task_time_textview);
        timeText.setText("");
        TextView whereText = row.findViewById(R.id.new_task_where_textview);
        whereText.setText(where);
        CheckBox checkBox = row.findViewById(R.id.new_task_checkbox);
        checkBox.setChecked(checked);

        // TODO: Where the index ID is saved into the view
        TextView index_ID_in_saved_events = row.findViewById(R.id.new_task_index_ID_in_saved_events);
        index_ID_in_saved_events.setText(uuid);

        // TODO: the clicklistener for the checkboxes
        // TODO: this code doesnt work, but the idea is there.
        // TODO: not sure but I was thinking you could reference the event in the 'copy' array which is updated,
        // TODO: and copy that event to the 'master' array?

        checkBox.setOnClickListener((View lview) -> {
            switch (lview.getId()) {
                // TODO: this R.id is in reference to the checkbox on that TableRow
                case R.id.new_task_checkbox:
                    //if (checkBox.isChecked()) {
                    // TODO: this is in reference to the specific row the checkbox is on
                    TableRow row1 = (TableRow) lview.getParent();
                    CheckBox rowCheckbox = row1.findViewById(R.id.new_task_checkbox);

                    // TODO: references and retrieves the index value stored in the view
                    TextView textView = row1.findViewById(R.id.new_task_index_ID_in_saved_events);
                    //final int indexOfChecked = Integer.parseInt(textView.getText().toString());

                    // TODO: attempts to create a new 'event' and change the category to completed, but the effects are saved.
                    // TODO: note: this is changing the copy of the array of objects
                    final String completed = "Completed";
                    Event newEvent = new Event(name, dateTime, where, category, notification, alarm, !checked);
                    EventManager.DeleteEvent(getContext(), uuid);
                    EventManager.AddEvent(newEvent, getContext());
                    onViewCreated(view, savedState);
                        /*
                        Event oldEvent = storedEvents.get(indexOfChecked);
                        EventManager.DeleteEvent(getContext(), oldEvent.uuid);
                        oldEvent.checked=!oldEvent.checked; //toggle checked
                        EventManager.AddEvent(oldEvent,getContext());*/
                        /*
                        Event completedEvent = new Event(storedEvents.get(indexOfChecked).name, storedEvents.get(indexOfChecked).dateTime, storedEvents.get(indexOfChecked).address, completed, storedEvents.get(indexOfChecked).notification, storedEvents.get(indexOfChecked).alarm);
                                //name, datetime, address, category, notification, alarm
                        //storedEvents.set(indexOfChecked, completedEvent);
                        EventManager.DeleteEvent(getContext(),storedEvents.get(indexOfChecked));
                        EventManager.AddEvent(completedEvent,getContext());*/
                    break;

                //} else {
                /*
                        // TODO: this is in reference to the specific row the checkbox is on
                        TableRow row1 = (TableRow) view.getParent();
                        CheckBox rowCheckbox = row1.findViewById(R.id.new_task_checkbox);

                        // TODO: references and retrieves the index value stored in the view
                        TextView IDtextView = row1.findViewById(R.id.new_task_index_ID_in_saved_events);
                        final int indexOfChecked = Integer.parseInt(IDtextView.getText().toString());

                        // TODO: retrieves the previous category of the task, so it could be reverted back to the correct category
                        TextView savedCateogry = row1.findViewById(R.id.savedCategory);
                        final String category1 = savedCateogry.getText().toString();

                        // TODO: attempts to create a new 'event' and change the category to completed, but the effects are saved.
                        // TODO: note: this is changing the copy of the array of objects
                        Event revertedEvent = new Event(storedEvents.get(indexOfChecked).name, storedEvents.get(indexOfChecked).dateTime, storedEvents.get(indexOfChecked).address, category1, storedEvents.get(indexOfChecked).notification, storedEvents.get(indexOfChecked).alarm);
                        storedEvents.set(indexOfChecked, revertedEvent);
                        break;*/
                //}
            }
        });

        if (checked) {
            completedTable.addView(row);
        } else {
            switch (category) {
                case "School":
                    schoolTable.addView(row);
                    break;
                case "Errand":
                    errandsTable.addView(row);
                    break;
                case "Phone Call":
                    phoneTable.addView(row);
                    break;
                case "Other":
                    otherTable.addView(row);
                    break;
            }
            /*
                // TODO: added the "Completed" category
            case "Completed":
                completedTable.addView(row);
                break;
                */
        }
    }

    public String setDate() {
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy");//formating according to my need
        String date = formatter.format(today);
        return date;
    }
}