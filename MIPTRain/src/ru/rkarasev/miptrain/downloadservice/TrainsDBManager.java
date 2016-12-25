package ru.rkarasev.miptrain.downloadservice;

import java.util.ArrayList;
import java.util.Hashtable;

import ru.rkarasev.miptrain.utils.Train;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TrainsDBManager {
	private Context context;
	public TrainsDBManager(Context context) {
		this.context = context;
	}
	String [] values = {"пл. Долгопрудная",
			"пл. Новодачная",
			"Марк",
			"пл. Лианозово",
			"Бескудниково",
			"пл. Дегунино",
			"пл. Окружная",
			"пл. Тимирязевская",
			"Москва (Савёловский вокзал)"};
	String[] refvalues = {
 			"dolg",
 			"novo",
 			"mark",
 			"lian",
 			"besk",
 			"degu",
 			"okry",
 			"timi",
 			"savy"
	};
	public void insertData(ArrayList<Train> data) {
		 ContentValues trainValue = new ContentValues();
		 SQLiteDatabase db = (new TrainsDBHelper(context)).getWritableDatabase();
		 for (Train train : data) {
             trainValue.put("number", train.getNumber());
             trainValue.put("direction", train.getDirection());
             trainValue.put("scheduleMode", train.getScheduleMode());
             trainValue.put("exceptionDays", train.getExceptionDays());
             for (int i = 0; i < 9; i++) {
            	 trainValue.put(refvalues[i], train.getStations().get(values[i]));
             }
             db.insert("trainsTableVer2", null, trainValue);
             System.out.println("Inserted train, number = " + train.getNumber() + ", direction = " + train.getDirection() + ", scheduleMode = " + train.getScheduleMode());
             
         }
		 db.close();
	}
	
	public void deleteData(int scheduleMode) {
		 SQLiteDatabase db = (new TrainsDBHelper(context)).getWritableDatabase();
		 System.out.println("DELETED " + db.delete("trainsTableVer2", "scheduleMode = " + Integer.toString(scheduleMode), null) + " TRAINS");
		 db.close();
	}
	
	public ArrayList<Train> getData() {
		 SQLiteDatabase db = (new TrainsDBHelper(context)).getWritableDatabase(); 
		 ArrayList<Train> backData = new ArrayList<Train>();
		 Cursor c = db.query("trainsTableVer2", null, null, null, null, null, null);
		 if (c.moveToFirst()) {
			 do {
				 	Hashtable<String, String> stations = new Hashtable<String, String>();
				 	String number = c.getString(c.getColumnIndex("number"));
				 	String exceptionDays = c.getString(c.getColumnIndex("exceptionDays"));
				 	int scheduleMode = c.getInt(c.getColumnIndex("scheduleMode"));
				 	int direction = c.getInt(c.getColumnIndex("direction"));
				 	for (int i = 0; i < 9; i++) {
				 		stations.put(values[i], c.getString(c.getColumnIndex(refvalues[i])));
				 	}
				 	Train train = new Train(number, stations, direction, scheduleMode, exceptionDays);
				 	backData.add(train);
		        } while (c.moveToNext());
		 }
		 db.close();
		 return backData;
	}
	
}
