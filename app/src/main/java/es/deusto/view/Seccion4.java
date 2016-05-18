package es.deusto.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.elpoeta.menulateralslide.R;

/**
 * Created by user on 26/08/2014.
 */
public class Seccion4 extends Fragment {

    EditText eTextName, eTextUrl;
    Button bSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.seccion4, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        eTextName = (EditText) rootView.findViewById(R.id.tEditNameRss);
        eTextUrl = (EditText) rootView.findViewById(R.id.tEditUrl);
        bSave = (Button) rootView.findViewById(R.id.buttonSave);
        startEvents();
    }
    private void startEvents() {
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("asd", "asd");
            }
        });
    }
}
