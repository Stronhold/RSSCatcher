package es.deusto.model.services.database;

import java.util.List;

import es.deusto.model.services.database.dao.RSS;
import es.deusto.model.services.database.dao.RSSDao;

/**
 * Created by Sergio on 13/05/2016.
 */
public class ServiceRSS {
    private final RSSDao rss;

    public ServiceRSS(RSSDao rssDao){
        this.rss = rssDao;
    }

    public List<RSS> getRSS(){
        return rss.queryBuilder().list();
    }

    public void deleteAllRSS(){
        rss.deleteAll();
    }

    public void insertRSSs(List<RSS> rss){
        for (RSS r: rss ) {
            this.rss.insert(r);
        }
    }

    public boolean insertRSS(RSS r) {
        if(getRSS().size() >= 3){
            return false;
        }
        rss.insert(r);
        return true;
    }
}
