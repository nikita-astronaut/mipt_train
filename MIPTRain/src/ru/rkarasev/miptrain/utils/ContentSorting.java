package ru.rkarasev.miptrain.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

public class ContentSorting {
	public final static int TO = 0;
    public final static int FROM = 1;
	private SharedPreferences startPref;
	private int direction;
	public ContentSorting (Context context, int direction) {
		this.startPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
		this.direction = direction;
	}
	public boolean isFit(Train train, int mode) {

		if (mode == 1) {
			if (train.getScheduleMode() == 4) {
				return false;
			}
			if (train.getScheduleMode() == 3) {
				if (!Utils.isToday(train.getExceptionDays())) {
					return false;
				}
			}
		}

		String dayOfTheWeek = (new SimpleDateFormat("EEEE")).format(new Date());
		
		int scheduleMode = train.getScheduleMode();
		int realMode;
		
		if ((dayOfTheWeek.equals("суббота")) || (dayOfTheWeek.equals("воскресенье")) || (dayOfTheWeek.equals("Sunday")) || (dayOfTheWeek.equals("Saturday"))) {
			realMode = 2;
		} else {
			realMode = 1;
		}
		
		if ((realMode == 1) && (scheduleMode == 2)) {
			return false;
		}
		if ((realMode == 2) && (scheduleMode == 1)) {
			return false;
		}
		
		if (direction != train.getDirection()) {
			return false;
		}
		
		String STARTTO  = startPref.getString("STARTTO", "");
		if (STARTTO.equals("")) {
			startPref.edit().putString("STARTTO", "NOVO");
			startPref.edit().commit();
			STARTTO = "NOVO";
		}
		String STARTFROM  = startPref.getString("STARTFROM", "");
		
		if (STARTFROM.equals("")) {
			startPref.edit().putString("STARTFROM", "TIMI");
			startPref.edit().commit();
			STARTFROM = "TIMI";
		}
		
		if ((STARTTO.equals("NOVO")) && (train.getStations().get("пл. Новодачная").equals("null"))) {
			return false;
		}
		if ((STARTTO.equals("DOLG")) && (train.getStations().get("пл. Долгопрудная").equals("null"))) {
			return false;
		}
		if ((STARTFROM.equals("SAVY")) && (train.getStations().get("Москва (Савёловский вокзал)").equals("null"))) {
			return false;
		}
		if ((STARTFROM.equals("TIMI")) && (train.getStations().get("пл. Тимирязевская").equals("null"))) {
			return false;
		}
		
		if ((direction == TO) && (STARTTO.equals("NOVO")) && (Utils.compareTime(Utils.getCurrentTime(), train.getStations().get("пл. Новодачная")) == 1)) {
			return false;
		}
		if ((direction == TO) && (STARTTO.equals("DOLG")) && (Utils.compareTime(Utils.getCurrentTime(), train.getStations().get("пл. Долгопрудная")) == 1)) {
			return false;
		}
		if ((direction == FROM) && (STARTFROM.equals("TIMI")) && (Utils.compareTime(Utils.getCurrentTime(), train.getStations().get("пл. Тимирязевская")) == 1)) {
			return false;
		}
		if ((direction == FROM) && (STARTFROM.equals("SAVY")) && (Utils.compareTime(Utils.getCurrentTime(), train.getStations().get("Москва (Савёловский вокзал)")) == 1)) {
			return false;
		}
		return true;
	}
	public ArrayList<FinalData> sortContent (ArrayList<Train> content) {
		int mode;
		if (startPref.getString("REFRESHED", "").equals(Utils.getCurrentDate())) {
			mode = 0;
			System.out.println("База данных была обновлена сегодня, мамой клянусь");
		} else {
			mode = 1;
			System.out.println("База данных была обновлена не сегодня, мамой клянусь, REFRESHED = " + startPref.getString("REFRESHED", ""));
		}
		String STARTTO  = startPref.getString("STARTTO", "");
		String STARTFROM  = startPref.getString("STARTFROM", "");
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
		 
		if (content.isEmpty()) {
			System.out.println("EMPTY CONTENT");
			return null;
		} else {
			System.out.println("Content size = " + content.size());
			ArrayList<FinalData> backContent = new ArrayList<FinalData>();
			
			if (startPref.getString("ELNUMBER", "").equals("")) {
				startPref.edit().putString("ELNUMBER", "0").commit();
			}
			int maxNumber = Integer.parseInt(startPref.getString("ELNUMBER", "0")) + 1;
			
			int trainsAdded = 0;
			int iterator = 0;
			while ((Utils.compareTime("00:00", content.get(iterator).getStations().get("пл. Долгопрудная")) == -1) && (iterator < maxNumber - 1) && (iterator < content.size() - 1) ) {
				++iterator;
			}

			while ((trainsAdded < maxNumber) && (iterator < content.size())) {
				Train curTrain = content.get(iterator);
				if (isFit(curTrain, mode)) {					
					++trainsAdded;
					String myline = new String();
					String mytime = new String();
					String myShort = new String();
					for (int i = 0; i < 9; i++) {
						
						if ((curTrain.getStations().get(values[i]).equals("null") == false)  && (startPref.getString(refvalues[i], "").equals("Y"))) {
							myline = myline.concat(values[i]);
							myline = myline.concat("(" + curTrain.getStations().get(values[i]) + ") ");
						}
					}
					
					if ((direction == TO) && (STARTTO.equals("NOVO"))) {
						mytime = mytime.concat(curTrain.getStations().get("пл. Новодачная"));
					}
					
					if ((direction == TO) && (STARTTO.equals("DOLG"))) {
						mytime = mytime.concat(curTrain.getStations().get("пл. Долгопрудная"));
					}
					
					if ((direction == FROM) && (STARTFROM.equals("SAVY"))) {
						mytime = mytime.concat(curTrain.getStations().get("Москва (Савёловский вокзал)"));
					}
					
					if ((direction == FROM) && (STARTFROM.equals("TIMI"))) {
						mytime = mytime.concat(curTrain.getStations().get("пл. Тимирязевская"));
					}
					
					if (direction == TO) {
						if (STARTTO.equals("NOVO")) {
							myShort = myShort.concat("Новодачная ");
							myShort = myShort.concat("(" + curTrain.getStations().get("пл. Новодачная") + ")");
						}
						if (STARTTO.equals("DOLG")) {
							myShort = myShort.concat("Долгопрудная ");
							myShort = myShort.concat("(" + curTrain.getStations().get("пл. Долгопрудная") + ")");
						}
						if (STARTFROM.equals("TIMI")) {
							myShort = myShort.concat(" Тимирязевская ");
							myShort = myShort.concat("(" + curTrain.getStations().get("пл. Тимирязевская") + ")");
						}
						if (STARTFROM.equals("SAVY")) {
							myShort = myShort.concat(" Савёловская ");
							myShort = myShort.concat("(" + curTrain.getStations().get("Москва (Савёловский вокзал)") + ")");
						}					
					}
					if (direction == FROM) {
						if (STARTFROM.equals("TIMI")) {
							myShort = myShort.concat("Тимирязевская ");
							myShort = myShort.concat("(" + curTrain.getStations().get("пл. Тимирязевская") + ")");
						}
						if (STARTFROM.equals("SAVY")) {
							myShort = myShort.concat("Савёловская ");
							myShort = myShort.concat("(" + curTrain.getStations().get("Москва (Савёловский вокзал)") + ")");
						}	
						if (STARTTO.equals("NOVO")) {
							myShort = myShort.concat(" Новодачная ");
							myShort = myShort.concat("(" + curTrain.getStations().get("пл. Новодачная") + ")");
						}
						if (STARTTO.equals("DOLG")) {
							myShort = myShort.concat(" Долгопрудная ");
							myShort = myShort.concat("(" + curTrain.getStations().get("пл. Долгопрудная") + ")");
						}
					}
					FinalData data = new FinalData(myShort, Utils.getDifference(Utils.getCurrentTime(), mytime), myline, null);
					backContent.add(trainsAdded - 1, data);
				}
				++iterator;
			}
			return backContent;
		}	
	}
}
