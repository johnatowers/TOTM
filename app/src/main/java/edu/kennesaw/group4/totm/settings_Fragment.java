package edu.kennesaw.group4.totm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link settings_Fragment.OnFragmentInteractionListener} interface
 * create an instance of this fragment.
 */
public class settings_Fragment extends Fragment {

    Switch DarkMode;
    Switch Notifications;
    Button Feedback;
    Button ClearEvents;
    Intent Emailintent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "totm.team@gmail.com", null));



    private OnFragmentInteractionListener mListener;
    public settings_Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final View finalView=view;

       //The Builder For The Warning Text before User Clears All events
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are You sure?");
        //THE POSITIVE BUTTON
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventManager.deleteAllEvents(finalView.getContext());
                Toast.makeText(getActivity(), "Events Cleared", Toast.LENGTH_LONG).show();

            }
        });
        //THE NEGATIVE BUTTON
        builder.setNegativeButton("Never Mind", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_LONG).show();

            }
        });
        final AlertDialog dialog = builder.create();
        //THE CLEAR EVENTS BUTTON THAT CALLS THE BUILDER
        ClearEvents = view.findViewById(R.id.Clear_Events_Button);
        ClearEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        //THE FEEDBACK BUTTON, Opens up the default email application and sets the recipient field and the subject field
        Feedback = view.findViewById(R.id.Feedback_Button);
        Emailintent.putExtra(Intent.EXTRA_SUBJECT, "How did we Mess Up?");
        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(Emailintent, "Send Email"));
            }
        });

        //THE TOGGLE SWITCH FOR NOTIFICATIONS
        Notifications = view.findViewById(R.id.Disable_Notifications_Switch);
        Notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Toast.makeText(getActivity(), "Notifications Disabled", Toast.LENGTH_LONG).show();
                    TOTMPole.notificationsEnabled = (isChecked);
                }
                else
                    Toast.makeText(getActivity(), "Notifications Enabled", Toast.LENGTH_LONG).show();



            }
        });

        //THE TOGGLE SWITCH FOR THE DARK THEME
        DarkMode = view.findViewById(R.id.Toggle_Dark_Theme);
        DarkMode.setVisibility(View.INVISIBLE);
        DarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Toast.makeText(getActivity(), "Dark Mode Enabled", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity(), "Dark Mode Disabled", Toast.LENGTH_LONG).show();

            }
        });
        Objects.requireNonNull(getActivity()).setTitle("TOTM                                                                       Settings");
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
