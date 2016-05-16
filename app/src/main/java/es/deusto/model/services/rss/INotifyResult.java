package es.deusto.model.services.rss;

import java.util.List;

import es.deusto.model.services.database.dao.Noticia;

/**
 * Created by Sergio on 16/05/2016.
 */
public interface INotifyResult {
    void processFinish(List<Noticia> items);
}
