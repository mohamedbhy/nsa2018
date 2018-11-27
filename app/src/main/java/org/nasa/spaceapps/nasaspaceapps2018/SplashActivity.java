package org.nasa.spaceapps.nasaspaceapps2018;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class SplashActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable task;
    private ImageView loadro;
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("NSA", name, importance);
            channel.setLightColor(0x0000FF);
            channel.enableLights(true);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bgs));
        }
        setContentView(R.layout.activity_splash);
        this.loadro=(ImageView)findViewById(R.id.loadro);
        loadro.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotateload));
        createNotificationChannel();
        try {
            DataFactory.context = getApplicationContext();
            DataFactory.getInstance();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.task = new Runnable() {
            @Override
            public void run() {
                HashMap<String,Boolean> res = null;
                try {
                    res = new DataRetriver(SplashActivity.this).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("res",res);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        };
        this.handler = new Handler(Looper.getMainLooper()){
            @Override
            public synchronized void handleMessage(Message msg) {
                HashMap<String,Boolean> res = (HashMap<String, Boolean>) msg.getData().getSerializable("res");
                if(res.get("DBIErr") && res.get("NetworkErr")){
                    findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
                    findViewById(R.id.retry).setVisibility(View.VISIBLE);
                    findViewById(R.id.exit).setVisibility(View.VISIBLE);
                    findViewById(R.id.loadro).setVisibility(View.GONE);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    if(res.get("NetworkErr")){
                        Toast.makeText(SplashActivity.this, "No Internet Connexion Available", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(intent);
                }
            }
        };
        findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                findViewById(R.id.loadro).setVisibility(View.VISIBLE);
                findViewById(R.id.loadro).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotateload));
                findViewById(R.id.no_internet).setVisibility(View.GONE);
                findViewById(R.id.retry).setVisibility(View.GONE);
                findViewById(R.id.exit).setVisibility(View.GONE);
                Thread thread = new Thread(SplashActivity.this.task);
                thread.start();
            }
        });
        new Thread(this.task).start();
    }
}
