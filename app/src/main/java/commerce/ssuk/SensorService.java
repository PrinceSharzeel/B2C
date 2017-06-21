package commerce.ssuk;

/**
 * Created by prince on 18/6/17.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class SensorService extends Service {
    public int counter=0;
    public SensorService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");





    }

    public SensorService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 0, 3600000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {

getItems();
            Log.i("in timer", "in timer ++++  "+ (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }





    public void getItems() {

        Time now = new Time();
        now.setToNow();
        String sTime = now.format("%Y_%m_%d %T");
        //sTime="2017_06_23 23:23:51";
        int nowt,nowy,nowm,nowd,thent,theny,thenm,thend;
        nowy=Integer.parseInt(sTime.substring(0,4));
        nowm=Integer.parseInt(sTime.substring(5,7));
        nowd=Integer.parseInt(sTime.substring(8,10));
        nowt=Integer.parseInt(sTime.substring(11,13));

        try {
            final DBAdapter db = new DBAdapter(getApplicationContext());
            db.open();
            SharedPreferences pref=getApplication().getSharedPreferences("session",0);
            Cursor c = db.getAllCart(pref.getString("contact",null));
            if (c.moveToFirst()) {
                do {

                   String t= c.getString(4);


                    theny=Integer.parseInt(t.substring(0,4));
                    thenm=Integer.parseInt(t.substring(5,7));
                    thend=Integer.parseInt(t.substring(8,10));
                    thent=Integer.parseInt(t.substring(11,13));


                    if(thend+3<nowd||thenm<nowm||theny<nowy)
                    {

                            Log.e("Db","TimeUp");



                        db.deleteContact(c.getString(1),pref.getString("contact",null));





                    }



                    Log.e("Db",c.getString(4));
                    Log.e("Now",+nowt+" / "+nowy+" /  "+nowm+" / "+nowd);
                    Log.e("Then",+thent+" / "+theny+" / "+thenm+" / "+thend);

                } while (c.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            Log.e("Db", "Ds");
            e.printStackTrace();
        }


    }












}




