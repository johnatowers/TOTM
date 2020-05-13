
package edu.kennesaw.group4.totm;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.Calendar;


class Event extends JSONObject {
    String name;

    public Calendar dateTime;

    String address;
    String category;

    //duration in seconds before which notification or alarms are fired
    Calendar notification;
    Calendar alarm;
    boolean checked;
    String uuid;

    Event(String name, Calendar dateTime, String address, String category, Calendar notification, Calendar alarm, boolean checked) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.dateTime = dateTime;
        this.address = address;
        this.category = category;
        this.notification = notification;
        this.alarm = alarm;
        this.checked = checked;

    }

    String toJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("name", name);
        result.put("category", category);
        long dateTimeMillis = dateTime.getTimeInMillis();
        result.put("dateTime", dateTimeMillis);
        result.put("address", address);
        result.put("preNotiDuration", notification.getTimeInMillis());
        result.put("preAlarmDuration", alarm.getTimeInMillis());
        result.put("checked", checked);
        result.put("uuid", uuid);
        return result.toString(2);
    }

    static Event parseJSON(String JSON) throws JSONException {
        JSONObject jsonObject = new JSONObject(JSON);
        String name = jsonObject.getString("name");
        String category = jsonObject.getString("category");
        long millis = jsonObject.getLong("dateTime");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        String address = jsonObject.getString("address");
        long nmillis = jsonObject.getLong("preNotiDuration");
        Calendar n = Calendar.getInstance();
        n.setTimeInMillis(nmillis);
        long amillis = jsonObject.getLong("preAlarmDuration");
        Calendar a = Calendar.getInstance();
        a.setTimeInMillis(amillis);
        boolean checked = jsonObject.getBoolean("checked");
        String uuid = jsonObject.getString("uuid");
        Event result = new Event(name, c, address, category, n, a, checked);
        result.uuid = uuid;
        return result;
    }
}
