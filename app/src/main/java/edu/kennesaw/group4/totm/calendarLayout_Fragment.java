package edu.kennesaw.group4.totm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class calendarLayout_Fragment extends Fragment {

    CalendarView calendarView;

    TableLayout schoolTable;
    TableLayout errandsTable;
    TableLayout phoneTable;
    TableLayout otherTable;
    TableLayout completedTable;

    ArrayList<Event> todaysEvents;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_layout, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        schoolTable = view.findViewById(R.id.school_table);
        errandsTable = view.findViewById(R.id.errands_table);
        phoneTable = view.findViewById(R.id.phone_calls_table);
        otherTable = view.findViewById(R.id.other_table);
        completedTable = view.findViewById(R.id.completed_table);

        calendarView = view.findViewById(R.id.calendarView);

        ArrayList<Event> todaysEvents = new ArrayList<>();

        //loop through entire month
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 1; i <= 30; i++) {
            c.set(Calendar.DAY_OF_MONTH, i);
            todaysEvents.addAll(EventManager.getEvents(c, getActivity()));

        }

        schoolTable.removeAllViews();
        errandsTable.removeAllViews();
        phoneTable.removeAllViews();
        otherTable.removeAllViews();
        completedTable.removeAllViews();

        String dayOfMonth = getTodaysDay();
        String month = getTodaysMonth();
        String year = getTodaysYear();

        // Get the selected date
        String yearString = year;
        yearString = yearString.replaceFirst("^0+(?!$)", "");

        //month += 1;
        int month2 = Integer.parseInt(month);
        String monthString = Integer.toString(month2);
        monthString = monthString.replaceFirst("^0+(?!$)", "");

        String dayString = dayOfMonth;
        dayString = dayString.replaceFirst("^0+(?!$)", "");

        String msg = "Selected date is " + (month + 1) + "/" + dayOfMonth + "/" + year;
        //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        String selectedDate = monthString + "/" + dayString + "/" + yearString;

        // Clear the TableLayouts
        schoolTable.removeAllViews();
        errandsTable.removeAllViews();
        phoneTable.removeAllViews();
        otherTable.removeAllViews();
        completedTable.removeAllViews();

        // Now to get the dates from the array
        for (Event event : todaysEvents) {
            String name = event.name;
            String where = event.address;
            String category = event.category;
            Calendar dateTime = event.dateTime;
            String dateTimeString = MainScreen.CalendarToString(event.dateTime);

            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String parsedYear = yearFormat.format(dateTime.getTime());

            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            String parsedMonth = monthFormat.format(dateTime.getTime());
            parsedMonth = parsedMonth.replaceFirst("^0*", "");

            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            String parsedDay = dayFormat.format(dateTime.getTime());
            parsedDay = parsedDay.replaceFirst("^0*", "");

            String parsedDate = parsedMonth + "/" + parsedDay + "/" + parsedYear;
            //HAVE TO FORMAT PARSED DATE TO REMOVE ZERO's

            if (parsedDate.equals(selectedDate)) {
                makeTagGUI(event.uuid, name, dateTime, where, category, event.checked, event.notification, event.alarm, view, savedInstanceState);
            }

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                    // Get the selected date
                    String yearString = Integer.toString(year);
                    yearString = yearString.replaceFirst("^0+(?!$)", "");

                    //month += 1;
                    int month2 = month + 1;
                    String monthString = Integer.toString(month2);
                    monthString = monthString.replaceFirst("^0+(?!$)", "");

                    String dayString = Integer.toString(dayOfMonth);
                    dayString = dayString.replaceFirst("^0+(?!$)", "");

                    String msg = "Selected date is " + (month + 1) + "/" + dayOfMonth + "/" + year;
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    String selectedDate = monthString + "/" + dayString + "/" + yearString;

                    // Clear the TableLayouts
                    schoolTable.removeAllViews();
                    errandsTable.removeAllViews();
                    phoneTable.removeAllViews();
                    otherTable.removeAllViews();
                    completedTable.removeAllViews();

                    // Now to get the dates from the array
                    for (Event event : todaysEvents) {
                        String name = event.name;
                        String where = event.address;
                        String category = event.category;
                        Calendar dateTime = event.dateTime;
                        String dateTimeString = MainScreen.CalendarToString(event.dateTime);

                        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                        String parsedYear = yearFormat.format(dateTime.getTime());

                        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                        String parsedMonth = monthFormat.format(dateTime.getTime());
                        parsedMonth = parsedMonth.replaceFirst("^0*", "");

                        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                        String parsedDay = dayFormat.format(dateTime.getTime());
                        parsedDay = parsedDay.replaceFirst("^0*", "");

                        String parsedDate = parsedMonth + "/" + parsedDay + "/" + parsedYear;
                        //HAVE TO FORMAT PARSED DATE TO REMOVE ZERO's

                        if (parsedDate.equals(selectedDate)) {
                            makeTagGUI(event.uuid, name, dateTime, where, category, event.checked, event.notification, event.alarm, view, savedInstanceState);
                        }
                    }
                }
            });

            getActivity().setTitle("TOTM                                               Calendar");
        }
        Objects.requireNonNull(getActivity()).setTitle("TOTM                                                                      Calendar");
    }

    private void makeTagGUI(String uuid, String name, Calendar dateTime, String where, String category, boolean checked, Calendar notification, Calendar alarm, View view, Bundle savedState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.new_task_view_calendar, null);
        CheckBox checkBox = row.findViewById(R.id.new_task_checkbox);
        TextView nameText = row.findViewById(R.id.new_task_name_textview);
        nameText.setText(name);
        TextView dayText = row.findViewById(R.id.new_task_day_textview);
        dayText.setText(MainScreen.CalendarToString(dateTime));
        TextView timeText = row.findViewById(R.id.new_task_time_textview);
        timeText.setText("");
        TextView whereText = row.findViewById(R.id.new_task_where_textview);
        whereText.setText(where);
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

    public String getTodaysDay() {
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd");//formating according to my need
        String date = formatter.format(today);
        return date;
    }

    public String getTodaysMonth() {
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("MM");//formating according to my need
        String date = formatter.format(today);
        return date;
    }

    public String getTodaysYear() {
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");//formating according to my need
        String date = formatter.format(today);
        return date;
    }

}


//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link calendarLayout_Fragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// *
// */
//public class calendarLayout_Fragment extends Fragment {
//
//    private OnFragmentInteractionListener mListener;
//
//    EditText mDateEditText;
//    Calendar mCurrentDate;
//    Bitmap mGeneratedDateIcon;
//    ImageGenerator mImageGenerator;
//    ImageView mDisplayGeneratedImage;
//
//    public calendarLayout_Fragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_calendar_layout, container, false);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
