package es.deusto.model.services.database;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import es.deusto.model.services.database.dao.DaoMaster;
import es.deusto.model.services.database.dao.DaoSession;

/**
 * Created by Sergio on 13/05/2016.
 */
public class Database {

    private static Database d;
    private ServiceNews sNews;
    private ServiceRSS sRSS;
    private final static String DATABASE_NAME = "rsscatcher";
    private DaoMaster.DevOpenHelper helper;
    public DaoSession daoSession;
    private DaoMaster daoMaster;
    private SQLiteDatabase db;

    private Database(Context activityCaller){
        this.helper = new DaoMaster.DevOpenHelper(activityCaller, DATABASE_NAME, null);
        this.db = helper.getWritableDatabase();
        this.daoMaster = new DaoMaster(this.db);
        this.daoSession = this.daoMaster.newSession();
        sNews = new ServiceNews(daoSession.getNoticiaDao());
        sRSS = new ServiceRSS(daoSession.getRSSDao());
    }

    public static Database Instance(Context a){
        if(d == null){
            d = new Database(a);
        }
        return d;
    }

    public ServiceNews getNews(){
        return sNews;
    }

    public ServiceRSS getsRSS(){
        return sRSS;
    }


}
