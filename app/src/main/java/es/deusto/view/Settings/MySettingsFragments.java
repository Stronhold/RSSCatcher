package es.deusto.view.Settings;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.elpoeta.menulateralslide.R;

import java.util.Calendar;

import es.deusto.model.services.rss.RssService;

/**
 * Created by andre on 17/05/2016.
 */
public class MySettingsFragments extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    PendingIntent pendingIntent;

    private Activity activity;

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        this.activity = a;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(activity).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("notification")) {
            boolean b = sharedPreferences.getBoolean("notification", true);
            if (b) {
                startBackground();
            } else {
                finishBackground();
            }
        }
    }

    private void startBackground(){
        Log.i("REINICIAMOS SERVICIO", "AHORA");


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1800); // first time
        long frequency= 30 * 60 * 1000; // in ms

        Intent intent = new Intent(this.activity, RssService.class);
        pendingIntent = PendingIntent.getService(this.activity, 1205, intent, 0);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);
    }

    private void finishBackground(){
        Log.i("FINALIZAMOS SERVICIO", "AHORA");
        AlarmManager alarmManagerstop = (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);
        alarmManagerstop.cancel(pendingIntent);
        activity.stopService(new Intent(activity, RssService.class));
    }
}
