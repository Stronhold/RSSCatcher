package es.deusto.model.services.rss;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

import es.deusto.model.services.database.Database;
import es.deusto.model.services.database.dao.Noticia;
import es.deusto.model.services.database.dao.RSS;

/**
 * Created by Sergio on 16/05/2016.
 */
public class RssService extends Service implements INotifyResult{
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private List<Noticia> items;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        Database db = Database.Instance(this);
        List<RSS> listaRss = db.getsRSS().getRSS();
        // schedule task
        List<Noticia> listaNoticias = new ArrayList<Noticia>();

        for(RSS r:listaRss) {
            FeedTask myFeedTask = new FeedTask();
            try {
                myFeedTask.execute(r.getUrl()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            reloadDB(r.getId());
        }
    }

    public void reloadDB(Long id) {
        //Guardar en db
        int size = items.size();
        if(size > 3){
            size = 3;
        }
        Database.Instance(this).getNews().deleteAllRSSForANew(id);
        for(int i = 0; i < size; i++){
            Database.Instance(this).getNews().insertRSS(items.get(i));
        }
    }

    public void onDestroy()
    {
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void processFinish(List<Noticia> items) {
        this.items = items;
    }
}
