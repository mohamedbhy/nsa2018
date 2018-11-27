package org.nasa.spaceapps.nasaspaceapps2018;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NSAReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,NSAEventIntentService.class);
        intent1.putExtra("id",intent.getIntExtra("id",12));
        context.startActivity(new Intent(context,SplashActivity.class));
        context.startService(intent1);
    }
}
