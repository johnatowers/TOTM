package edu.kennesaw.group4.totm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class newItem_Fragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static Button daySelectSpinner;
    @SuppressLint("StaticFieldLeak")
    public static Button timeSelectSpinner;
    @SuppressLint("StaticFieldLeak")
    public static EditText whereEditText;
    @SuppressLint("StaticFieldLeak")
    public static Button timeSelectSpinner2;
    @SuppressLint("StaticFieldLeak")
    public static Button timeSelectSpinner3;

    public Spinner categorySpinner;

    EditText taskName;

    Switch reminderSwitch;
    Switch alarmSwitch;

    Button createEventButton;

    static Calendar currentDateTime;
    static Calendar notiDateTime;
    static Calendar alarmDateTime;
    static long nDiff = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_item, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        currentDateTime = Calendar.getInstance();
        currentDateTime.setTimeInMillis(0);
        notiDateTime = Calendar.getInstance();
        notiDateTime.setTimeInMillis(0);
        alarmDateTime = Calendar.getInstance();
        alarmDateTime.setTimeInMillis(0);

        // Setup any handles to view objects here
        taskName = view.findViewById(R.id.what_task_edittext);
        daySelectSpinner = view.findViewById(R.id.daySelectSpinner);
        timeSelectSpinner = view.findViewById(R.id.timeSelectSpinner);
        whereEditText = view.findViewById(R.id.where_edittext);

        timeSelectSpinner2 = view.findViewById(R.id.reminder_spinner);
        timeSelectSpinner3 = view.findViewById(R.id.alarm_spinner);

        categorySpinner = view.findViewById(R.id.category_spinner);

        //reminderSwitch = view.findViewById(R.id.reminder_switch);
        //alarmSwitch = view.findViewById(R.id.alarm_switch);

        createEventButton = view.findViewById(R.id.create_event_button);

        /////////////////////////////
        /////////////////////////////
        ///Picker Boxes//////////////
        /////////////////////////////
        /////////////////////////////
        //date picker box
        daySelectSpinner.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                assert getFragmentManager() != null;
                datePicker.show(getFragmentManager(), "datePicker");
            }
        });

        //time picker box
        timeSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                assert getFragmentManager() != null;
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });

        //time picker box
        timeSelectSpinner2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment2();
                assert getFragmentManager() != null;
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });

        //time picker box
        timeSelectSpinner3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment3();
                assert getFragmentManager() != null;
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });

        //create event
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create event
                String name = taskName.getText().toString();
                String address = whereEditText.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();

                Event newEvent = new Event(name, currentDateTime, address, category, notiDateTime, alarmDateTime, false);
                Context context = getActivity();
                EventManager.AddEvent(newEvent, context);
                //MainScreen.eventManager.AddEvent(newEvent);


                Toast.makeText(getActivity(), "Event Created", Toast.LENGTH_LONG).show();
            }
        });

        /////////////////////////////
        /////////////////////////////
        /////////////////////////////
        /////////////////////////////
        /////////////////////////////
        Objects.requireNonNull(getActivity()).setTitle("TOTM                                                                      New Task");
    }

    /////////////////////////////
    /////////////////////////////
    //Methods that make pickers//
    //set the EditTexts//////////
    /////////////////////////////
    /////////////////////////////
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            populateSetDate(year, month + 1, day);
            currentDateTime.set(Calendar.YEAR, year);
            currentDateTime.set(Calendar.MONTH, month);
            currentDateTime.set(Calendar.DAY_OF_MONTH, day);

            //set date for alarm and notifications as well
            notiDateTime.set(Calendar.YEAR, year);
            notiDateTime.set(Calendar.MONTH, month);
            notiDateTime.set(Calendar.DAY_OF_MONTH, day);

            alarmDateTime.set(Calendar.YEAR, year);
            alarmDateTime.set(Calendar.MONTH, month);
            alarmDateTime.set(Calendar.DAY_OF_MONTH, day);

        }

        public void populateSetDate(int year, int month, int day) {
            String setDay = month + "/" + day + "/" + year;
            daySelectSpinner.setText(setDay);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            populateSetTime(hourOfDay, minute);
            currentDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            currentDateTime.set(Calendar.MINUTE, minute);

        }

        public void populateSetTime(int hour, int minute) {
            String setTime = ((hour % 12 == 0) ? "12" : String.valueOf(hour % 12)) + ":" + (minute > 9 ? minute : "0" + minute) + " " + ((hour >= 12) ? "PM" : "AM");
            timeSelectSpinner.setText(setTime);
        }
    }



    public static class TimePickerFragment2 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            populateSetTime(hourOfDay, minute);
            notiDateTime.setTimeInMillis(System.currentTimeMillis());
            notiDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            notiDateTime.set(Calendar.MINUTE, minute);
        }

        public void populateSetTime(int hour, int minute) {
            String setTime = ((hour % 12 == 0) ? "12" : String.valueOf(hour % 12)) + ":" + (minute > 9 ? minute : "0" + minute) + " " + ((hour >= 12) ? "PM" : "AM");
            timeSelectSpinner2.setText(setTime);
        }
    }



    public static class TimePickerFragment3 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            populateSetTime(hourOfDay, minute);
            alarmDateTime.setTimeInMillis(System.currentTimeMillis());
            alarmDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            alarmDateTime.set(Calendar.MINUTE, minute);

        }

        public void populateSetTime(int hour, int minute) {
            String setTime = ((hour % 12 == 0) ? "12" : String.valueOf(hour % 12)) + ":" + (minute > 9 ? minute : "0" + minute) + " " + ((hour >= 12) ? "PM" : "AM");
            timeSelectSpinner3.setText(setTime);
        }
    }
    /////////////////////////////
    /////////////////////////////
    /////////////////////////////
    /////////////////////////////
    /////////////////////////////
}


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link newItem_Fragment.OnFragmentInteractionListener} interface
// * create an instance of this fragment.
// */
//public class newItem_Fragment extends Fragment {
//
//    private OnFragmentInteractionListener mListener;
//
//    public newItem_Fragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_new_item, container, false);
//
//        //date picker box
//        view.findViewById(R.id.editText3).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    DialogFragment datePicker = new DatePickerFragment();
//                    datePicker.show(getFragmentManager(), "datePicker");
//                    return true;
//                } else
//                    return false;
//            }
//        });
//
//        //time picker box
//        view.findViewById(R.id.editText2).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    DialogFragment timePicker = new TimePickerFragment();
//                    timePicker.show(getFragmentManager(), "timePicker");
//                    return true;
//                }
//                else
//                    return false;
//
//            }
//        });
//
//        return view;
//    }
//
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
//
//    public static class TimePickerFragment extends DialogFragment
//            implements TimePickerDialog.OnTimeSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current time as the default values for the picker
//            final Calendar c = Calendar.getInstance();
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//
//            // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute,
//                    DateFormat.is24HourFormat(getActivity()));
//        }
//
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            // Do something with the time chosen by the user
//        }
//    }
//
//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            // Do something with the date chosen by the user
//        }
//    }
//}
