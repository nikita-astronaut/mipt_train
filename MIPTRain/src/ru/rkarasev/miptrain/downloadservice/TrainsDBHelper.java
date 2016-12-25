package ru.rkarasev.miptrain.downloadservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class TrainsDBHelper extends SQLiteOpenHelper {

    public TrainsDBHelper(Context context) {
      super(context, "TrainsDBver2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table trainsTableVer2 ("
          + "id integer primary key autoincrement," 
          + "number text,"
          + "direction integer,"
          + "scheduleMode integer,"
          + "exceptionDays text,"
          + "novo text,"
          + "dolg text," 
          + "mark text," 
          + "lian text,"
          + "degu text," 
          + "besk text,"
          + "okry text,"
          + "timi text," 
          + "savy text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
  }
