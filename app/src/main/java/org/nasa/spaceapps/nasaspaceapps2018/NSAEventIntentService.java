package org.nasa.spaceapps.nasaspaceapps2018;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class NSAEventIntentService extends IntentService {
    public NSAEventIntentService() {
        super("NSAEventIntentService");
    }
    public static void CreateAlarm(int id, Context context){
        Intent notifyIntent = new Intent(context,NSAReceiver.class);
        notifyIntent.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,2,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar time = Calendar.getInstance();
        try {
            time.setTime(DataFactory.getInstance().getItemDetails(id).getLaunchDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP,time.getTimeInMillis(),pendingIntent);
    }
    public static void CancelAlarm(int id,Context context){
        Intent notifyIntent = new Intent(context,NSAReceiver.class);
        notifyIntent.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,2,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DataFactory.context=getApplicationContext();
        int id = intent.getIntExtra("id",12);
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this,"NSA");
        }else builder = new Notification.Builder(this);
        try {
            try {
                builder.setContentTitle(DataFactory.getInstance().getItemDetails(id).getMissionName())
                        .setContentText(DataFactory.getInstance().getItemDetails(id).getLaunchSite())
                        .setLargeIcon(Picasso.get().load(DataFactory.getInstance().getItemDetails(id).getImage()).get())
                        .setSmallIcon(R.drawable.rocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent notifyIntent = new Intent(getApplicationContext(),DetailsActivity.class);
        notifyIntent.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),2,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        notificationCompat.flags= Notification.FLAG_AUTO_CANCEL;
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(id,notificationCompat);

    }
}
