package es.deusto.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elpoeta.menulateralslide.R;

import es.deusto.view.Settings.MySettingsFragments;


/**
 * Created by andre on 17/05/2016.
 */
public class MySettings extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.mysettings, container, false);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MySettingsFragments())
                .commit();
        return rootView;
    }
}


