package es.deusto.view.Fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.elpoeta.menulateralslide.R;

import java.util.ArrayList;
import java.util.List;

import es.deusto.model.services.database.Database;
import es.deusto.model.services.database.dao.Noticia;
import es.deusto.model.services.database.dao.RSS;
import es.deusto.view.Fragments.Adapter.ListAdapterWithImage;

/**
 * Created by user on 26/08/2014.
 */
public class NewsFragment extends Fragment {

    ListView listaNews;
    ListAdapterWithImage adapter;
    private List<Noticia> noticias;
    private RSS rss;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.seccion1, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        listaNews = (ListView) rootView.findViewById(R.id.listaNews);
        if(noticias == null)
            noticias = new ArrayList<Noticia>();
        adapter = new ListAdapterWithImage(this.getActivity(), noticias);
        listaNews.setAdapter(adapter);
        events();
    }

    private void events() {
        listaNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter != null) {
                    if(adapter.data != null) {
                        if(adapter.data.size() > position) {
                            String url = adapter.data.get(position).getLink();
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setPackage("com.android.chrome");

                            try {
                                getActivity().startActivity(i);
                            } catch (ActivityNotFoundException ex) {
                                // Chrome browser presumably not installed so allow user to choose instead
                                i.setPackage(null);
                                try{
                                startActivity(i);
                                }catch (Exception e){
                                    Toast.makeText(getActivity(), "Url malformed", Toast.LENGTH_LONG);
                                }

                            }
                            //i.setData(Uri.parse(url));
                        }
                    }
                }
            }
        });
    }

    public void setInfoForNews(RSS rss) {
        try {
            noticias = Database.Instance(this.getActivity()).getNews().getNews(rss);
            this.rss = rss;
            if (adapter != null)
                adapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    public void reloadRSS(){
        setInfoForNews(rss);
    }
}
