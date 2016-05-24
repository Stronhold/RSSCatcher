package es.deusto.model.services.rss;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.elpoeta.menulateralslide.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.deusto.model.services.database.Database;
import es.deusto.model.services.database.dao.Noticia;
import es.deusto.model.services.database.dao.RSS;
import es.deusto.model.services.rss.task.FeedTask;

/**
 * Created by Sergio on 16/05/2016.
 */
public class RssService extends Service implements INotifyResult{
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private List<Noticia> items;
    int count = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "Start");

        Database db = Database.Instance(this);
        List<RSS> listaRss = db.getsRSS().getRSS();
        // schedule task
        List<Noticia> listaNoticias = new ArrayList<Noticia>();

        for(RSS r:listaRss) {
            FeedTask myFeedTask = new FeedTask(this);
            try {
                myFeedTask.execute(r.getUrl()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            reloadDB(r.getId());
        }

        if(count >= 0){
            String sendMsg = "Tienes " + count + " nuevas noticias";
            showNotification(getApplicationContext(), sendMsg);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service", "Stop");
        stopSelf();
        removeNotification(getApplicationContext());
    }

    private void showNotification(Context context, String message) {
        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.abc_cab_background_bottom_holo_dark)
                        .setContentTitle(message)
                        .setContentText(message);
        Notification noti = nBuilder.build();
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, noti);
    }

    private void removeNotification(Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(0);
    }


    @Override
    public void onCreate(){

        Database db = Database.Instance(this);
        List<RSS> listaRss = db.getsRSS().getRSS();
        // schedule task
        List<Noticia> listaNoticias = new ArrayList<Noticia>();

        for(RSS r:listaRss) {
            FeedTask myFeedTask = new FeedTask(this);
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
            Database.Instance(this).getNews().insertNews(items.get(i));
        }
    }

    @Override
    public void processFinish(List<Noticia> items) {
        this.items = items;
    }
}
