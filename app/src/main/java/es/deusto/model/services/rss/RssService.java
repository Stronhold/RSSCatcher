package es.deusto.model.services.rss;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.elpoeta.menulateralslide.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.deusto.model.services.Widget.WidgetProvider;
import es.deusto.model.services.database.Database;
import es.deusto.model.services.database.dao.Noticia;
import es.deusto.model.services.database.dao.RSS;
import es.deusto.model.services.rss.task.FeedTask;
import es.deusto.view.MyActivity;

/**
 * Created by Sergio on 16/05/2016.
 */
public class RssService extends Service implements INotifyResult{
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    boolean update = false;
    private int id;
    SharedPreferences sharedPref;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "Start");
        Boolean newNews = false;

        Database db = Database.Instance(this);
        List<RSS> listaRss = db.getsRSS().getRSS();
        // schedule task
        id = 0;
        if(checkInternet()) {
            Database.Instance(this).getNews().deleteAllNews();
            for (RSS r : listaRss) {
                FeedTask myFeedTask = new FeedTask(this);
                try {
                    myFeedTask.setID(r.getId());
                    myFeedTask.execute(r.getUrl()).get();
                    newNews = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            boolean notifications = sharedPref.getBoolean("notification", true);
            if (notifications && newNews) {
                String sendMsg = "Tienes nuevas noticias";
                showNotification(getApplicationContext(), sendMsg);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean checkInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) this.getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service", "Stop");
        stopSelf();
        removeNotification(getApplicationContext());
    }

    private void showNotification(Context context, String message) {
        Intent defineIntent = new Intent(this, MyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, defineIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(message)
                        .setContentText(message)
                        .setContentIntent(pendingIntent);
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
        }
    }

    public void reloadDB(List<Noticia> items,Long id) {
        //Guardar en db
        if(items != null) {
            int size = items.size();
            Noticia news = items.get(0);
            for (int i = 0; i < size; i++) {
                Noticia n = items.get(i);
                n.setNoticiaID(id);
                n.setId((long) this.id);
                this.id++;
                Database.Instance(this).getNews().insertNews(n);
            }
            SendInfoToWidget(news);
        }
    }

    @Override
    public void processFinish(List<Noticia> items, long id) {
        reloadDB(items, id);
    }

    public void SendInfoToWidget (Noticia n)
    {
        RemoteViews updateViews = new RemoteViews(this.getBaseContext().getPackageName(), R.layout.mywidget);
        if(n != null) {
            if(n.getImage() != null) {
                Uri u = Uri.parse(n.getImage());
                updateViews.setImageViewUri(R.id.imageWidget, u);
            }
            updateViews.setTextViewText(R.id.txtNombre, n.getTitulo());
            // When user clicks on widget, launch to Wiktionary definition page

            Intent defineIntent = new Intent(this, MyActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, defineIntent, 0);
            updateViews.setOnClickPendingIntent(R.id.txtNombre, pendingIntent);
            updateViews.setOnClickPendingIntent(R.id.imageWidget, pendingIntent);


            ComponentName thisWidget = new ComponentName(this, WidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
        }

    }

}
