package edu.kennesaw.group4.totm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class TOTMPole extends Service {
    static ArrayList<Event> events = new ArrayList<>();
    static ArrayList<AlarmManager.OnAlarmListener> listeners = new ArrayList<>();

    public TOTMPole() {
        events = new ArrayList<>();
    }

    static boolean notificationsEnabled = true;
    static final String id = "totemnoti";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleNext48(this);
        return super.onStartCommand(intent, flags, startId);
    }

    static void scheduleNext48(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, "Totm Notification Channel", importance);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            //mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        final Context fcontext = context;
        cancelAll(context);

        //Schedule re-scheduling of events (failsafe)
        AlarmManager.OnAlarmListener reScedule = new AlarmManager.OnAlarmListener() {
            @Override
            public void onAlarm() {
                scheduleNext48(fcontext);
            }
        };
        listeners.add(reScedule);
        long inTwelveHours = System.currentTimeMillis() + (12 * 60 * 60 * 1000);
        manager.set(AlarmManager.RTC_WAKEUP, inTwelveHours, "Reschedule", reScedule, null);

        // Schedule events for the next 2 days
        Calendar c = Calendar.getInstance();
        events.clear();
        events.addAll(EventManager.getEvents(c, context)); //get today's events
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1); //move calendar forward by 1 day
        events.addAll(EventManager.getEvents(c, context)); //get tomorrow's events

        //schedule all events' notifications and alarms, if enabled
        for (final Event event : events) {
            final long targetTimeUTC = event.notification.getTimeInMillis();
            //schedule notification if enabled, and if the event has not already passed
            if (targetTimeUTC > System.currentTimeMillis() && notificationsEnabled) {
                Intent navIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + event.address));
                PendingIntent pendingIntent = PendingIntent.getActivity(fcontext, 0, navIntent, 0);
                AlarmManager.OnAlarmListener listener = () -> {
                    Notification n = new NotificationCompat.Builder(fcontext, id)
                            .setContentTitle(event.name)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(event.address + ", " + MainScreen.CalendarToString(event.dateTime)))
                            .addAction(0, "Navigate", pendingIntent)
                            .setColor(fcontext.getColor(R.color.colorPrimary))
                            .build();
                    ((NotificationManager) fcontext.getSystemService(NOTIFICATION_SERVICE)).notify(0, n);
                };
                listeners.add(listener);
                manager.setExact(AlarmManager.RTC_WAKEUP, targetTimeUTC-30000, "Notification", listener, null);
            }

            //schedule alarm
            final long targetATimeUTC = event.notification.getTimeInMillis();
            //schedule notification if enabled, and if the event has not already passed
            if (targetATimeUTC > System.currentTimeMillis() && notificationsEnabled) {
                AlarmManager.OnAlarmListener listener = () -> {
                    Intent alarming = new Intent(fcontext, AlarmActivity.class);
                    alarming.putExtra("name", event.name);
                    alarming.putExtra("time", MainScreen.CalendarToString(event.dateTime));
                    alarming.putExtra("where", event.address);
                    fcontext.startActivity(alarming);
                };
                listeners.add(listener);
                manager.setExact(AlarmManager.RTC_WAKEUP, targetATimeUTC-30000, "Alarm", listener, null);
            }
        }
    }

    static void cancelAll(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        for (AlarmManager.OnAlarmListener listener : listeners) {
            manager.cancel(listener);
        }
        listeners.clear();
    }
}
