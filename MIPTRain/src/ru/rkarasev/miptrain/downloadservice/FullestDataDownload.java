package ru.rkarasev.miptrain.downloadservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

public class FullestDataDownload extends BroadcastReceiver {
	final public static String ONE_TIME = "onetime";
	@Override
	public void onReceive(Context context, Intent intent) {
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
         Toast.makeText(context, "База данных начала обновляться", Toast.LENGTH_SHORT).show();
         System.out.println("База данных начала обновляться");
         //Acquire the lock
         wl.acquire();
         try {
         // Do the job here
         Intent myIntent = new Intent(context, DownloadService.class);
         context.startService(myIntent);
         } finally {
         //Release the lock
         wl.release();
		}
	}
	public void setDownload(Context context, long period){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, FullestDataDownload.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), period , pi);
        Toast.makeText(context, "Автоматическое обновление базы данных установлено", Toast.LENGTH_SHORT).show();
        System.out.println("Автоматическое обновление базы данных установлено");
    }

    public void CancelDownload(Context context){
        Intent intent = new Intent(context, FullestDataDownload.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
