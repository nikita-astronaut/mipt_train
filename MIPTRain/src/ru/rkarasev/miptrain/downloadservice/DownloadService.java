package ru.rkarasev.miptrain.downloadservice;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;


public class DownloadService extends Service {
	private static boolean isUpdating = false;
	public synchronized static boolean mayUpdate(){
		if(isUpdating){
			return false;
		} else {
			isUpdating = true;
			return true;
		}
	}
	public synchronized static void updated(){
		isUpdating = true;
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override

	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("Service created");
	}
	@Override

	public void onDestroy() {

		// TODO Auto-generated method stub

		super.onDestroy();

		Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();

	}
	 public int onStartCommand(Intent intent, int flags, int startId) {
		 System.out.println("Service started");
		 
         if(mayUpdate()){
        	 FullestInformationDownloadThread fidt = new FullestInformationDownloadThread(this);
             Thread thread = new Thread(fidt);
        	 thread.start();
         }
		 return Service.START_NOT_STICKY;
		  }
	 

}
