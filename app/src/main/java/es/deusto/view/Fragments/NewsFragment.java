package es.deusto.view.Fragments;

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
    }

    public void setInfoForNews(int news) {
        try {
            RSS rss = Database.Instance(this.getActivity()).getsRSS().getRSS().get(news);
            if (Database.Instance(this.getActivity()).getNews().getNews(rss).size() == 0) {
                Database.Instance(this.getActivity()).getNews().insertNews(new Noticia(rss.getId(), "hola" + news, "guapo", "", ""));
            }
            noticias = Database.Instance(this.getActivity()).getNews().getNews(rss);
            if (adapter != null)
                adapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }
}
