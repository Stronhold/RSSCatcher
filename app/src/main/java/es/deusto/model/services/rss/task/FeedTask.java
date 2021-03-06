package es.deusto.model.services.rss.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.deusto.model.services.database.dao.Noticia;
import es.deusto.model.services.rss.INotifyResult;
import es.deusto.model.services.rss.handler.items.RssItem;
import es.deusto.model.services.rss.Communication.RssReader;

/**
 * Created by Sergio on 16/05/2016.
 */
public class FeedTask extends AsyncTask<String, List<Noticia>, List<Noticia>>{
    public INotifyResult delegate = null;
    private long id;

    public FeedTask(INotifyResult result){
        this.delegate = result;
    }

    public void setID(long id){
        this.id = id;
    }
    @Override
    protected List<Noticia> doInBackground(String... params) {
        List<Noticia> noticias = new ArrayList<Noticia>();
        try {
            RssReader rssReader = new RssReader(params[0]);
            for (RssItem item : rssReader.getItems())
            {
                //String titulo, String descripcion, String image, String link
                Noticia n = new Noticia(1L,this.id, item.getTitle(),item.getDescription(), item.getImageUrl(), item.getLink());
                noticias.add(n);
            }
              //  adapter.add(item.getTitle());
        } catch (Exception e) {
            Log.v("Error Parsing Data", e + "");
        }
        return noticias;
    }

    @Override
    protected void onPostExecute(List<Noticia> n) {
        super.onPostExecute(n);
        if(delegate != null)
            delegate.processFinish(n, this.id);
    }
}
