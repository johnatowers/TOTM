package edu.kennesaw.group4.totm;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainScreen extends AppCompatActivity implements /*Today_Fragment.OnFragmentInteractionListener,*/
        /*calendarLayout_Fragment.OnFragmentInteractionListener,*/
        /*newItem_Fragment.OnFragmentInteractionListener,*/
        settings_Fragment.OnFragmentInteractionListener {

    //public static EventManager eventManager;
    Fragment currentFragment = null;
    FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_today:
                    currentFragment = new Today_Fragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
                case R.id.nav_calendar:
                    currentFragment = new calendarLayout_Fragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
                case R.id.nav_newEvent:
                    currentFragment = new newItem_Fragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
                case R.id.nav_settings:
                    currentFragment = new settings_Fragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TOTMPole.scheduleNext48(this);
        setContentView(R.layout.activity_main_screen);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new Today_Fragment();
        ft.replace(R.id.content, currentFragment);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void startTOTEMPole() {
        Intent i = new Intent(this, TOTMPole.class);
        startService(i);
    }

    public static String CalendarToString(Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, h:mm a", Locale.US);
        return sdf.format(c.getTime());
    }

}
