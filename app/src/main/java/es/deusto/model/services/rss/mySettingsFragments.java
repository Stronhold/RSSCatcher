package es.deusto.model.services.rss;

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

/**
 * Created by andre on 17/05/2016.
 */
public class mySettingsFragments extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    PendingIntent pendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

        EditTextPreference pref = (EditTextPreference) findPreference("txtLocDefault");
        pref.setSummary(pref.getText());
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
        calendar.add(Calendar.SECOND, 60); // first time
        long frequency= 30 * 60 * 1000; // in ms

        Intent intent = new Intent(this.getActivity(), RssService.class);
        pendingIntent = PendingIntent.getService(this.getActivity(), 123456, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);
    }

    private void finishBackground(){
        Log.i("FINALIZAMOS SERVICIO", "AHORA");
        AlarmManager alarmManagerstop = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        alarmManagerstop.cancel(pendingIntent);
        getActivity().stopService(new Intent(getActivity(), RssService.class));
    }
}
