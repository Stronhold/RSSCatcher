package es.deusto.view.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elpoeta.menulateralslide.R;

import es.deusto.model.services.database.dao.RSS;

/**
 * Created by Sergio on 18/05/2016.
 */
public class FragmentAddRss extends Fragment {
    EditText eTextName, eTextUrl;
    Button bSave;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_rss, container, false);

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
