package es.deusto.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.elpoeta.menulateralslide.R;

import java.util.ArrayList;
import java.util.List;

import es.deusto.model.services.database.dao.Noticia;
import es.deusto.view.Fragments.Adapter.ListAdapterWithImage;

/**
 * Created by user on 26/08/2014.
 */
public class Seccion1 extends Fragment {

    ListView listaNews;
    ListAdapterWithImage adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.seccion1, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        listaNews = (ListView) rootView.findViewById(R.id.listaNews);
        List<Noticia> algo = new ArrayList<Noticia>();
        algo.add(new Noticia((long) 0, "","","",""));
        adapter = new ListAdapterWithImage(this.getActivity(), algo);
        listaNews.setAdapter(adapter);
    }
}
