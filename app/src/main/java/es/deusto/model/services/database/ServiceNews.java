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

    public List<Noticia> getNews(RSS rss){
        return this.noticias.queryBuilder().where(NoticiaDao.Properties.NoticiaID.eq(rss.getId())).list();
    }

    public void deleteAllNews(){
        noticias.deleteAll();
    }

    public void insertMultipleNews(List<Noticia> news){
        for (Noticia n: news ) {
            this.noticias.insert(n);
        }
    }

    public void deleteAllRSSForANew(Long id) {
        List<Noticia> a = this.noticias.queryBuilder().where(NoticiaDao.Properties.NoticiaID.eq(id)).list();
        this.deleteListOfNews(a);
    }

    private void deleteListOfNews(List<Noticia> a) {
        for(Noticia n: a){
            noticias.delete(n);
        }
    }

    public void insertNews(Noticia noticia) {
        noticias.insert(noticia);
    }

    public void getAllNews() {
        List<Noticia> algo = noticias.queryBuilder().list();
    }

    public Noticia getNewsByID(long i) {
        Noticia n = noticias.load(i);
        return n;
    }
}
