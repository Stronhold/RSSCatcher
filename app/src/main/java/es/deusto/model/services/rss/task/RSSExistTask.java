package es.deusto.model.services.rss.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import es.deusto.model.services.database.dao.Noticia;
import es.deusto.model.services.rss.Communication.RssReader;
import es.deusto.model.services.rss.INotifyResult;
import es.deusto.model.services.rss.handler.items.RssItem;
import es.deusto.view.Fragments.INotifyInternet;

/**
 * Created by Sergio on 30/05/2016.
 */
public class RSSExistTask extends AsyncTask<String, Boolean, Boolean> {
    public INotifyInternet delegate = null;

    public RSSExistTask(INotifyInternet result){
        this.delegate = result;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            RssReader rssReader = new RssReader(params[0]);
            if(rssReader.getItems() != null && rssReader.getItems().size() > 0){
                return true;
            }
            //  adapter.add(item.getTitle());
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        if(delegate != null)
            delegate.connectivity(b);
    }
}
