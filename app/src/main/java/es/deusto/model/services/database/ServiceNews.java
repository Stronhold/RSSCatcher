package es.deusto.model.services.database;

import java.util.List;

import es.deusto.model.services.database.dao.Noticia;
import es.deusto.model.services.database.dao.NoticiaDao;
import es.deusto.model.services.database.dao.RSS;
import es.deusto.model.services.database.dao.RSSDao;

/**
 * Created by Sergio on 13/05/2016.
 */
public class ServiceNews {
    private NoticiaDao noticias;

    public ServiceNews(NoticiaDao noticiaDao){
        this.noticias = noticiaDao;
    }

    public List<Noticia> getRSS(){
        return noticias.queryBuilder().list();
    }

    public void deleteAllRSS(){
        noticias.deleteAll();
    }

    public void insertRSSs(List<Noticia> news){
        for (Noticia n: news ) {
            this.noticias.insert(n);
        }
    }
}
