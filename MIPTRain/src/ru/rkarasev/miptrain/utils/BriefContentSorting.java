package ru.rkarasev.miptrain.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

public class BriefContentSorting {
	private SharedPreferences startPref;
	public final static int TO = 0;
    public final static int FROM = 1;
	private int direction;
	public BriefContentSorting (Context context, int direction) {
		this.startPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
		this.direction = direction;
	}
	public boolean isFit(BriefTrain train) {		
		if (direction != train.getDirection()) {
			return false;
		}
		if (Utils.compareTime(Utils.getCurrentTime(), train.getDepartureTime()) == 1) {
			return false;
		}
		
		String STARTTO  = startPref.getString("STARTTO", "");
		if (STARTTO.equals("")) {
			startPref.edit().putString("STARTTO", "NOVO").commit();
			STARTTO = "NOVO";
		}
		String STARTFROM  = startPref.getString("STARTFROM", "");
		
		if (STARTFROM.equals("")) {
			startPref.edit().putString("STARTFROM", "TIMI").commit();
			STARTFROM = "TIMI";
		}
		return true;
	}
	
	public ArrayList<FinalData> sortContent (ArrayList<BriefTrain> content) {
		String STARTTO  = startPref.getString("STARTTO", "");
		String STARTFROM  = startPref.getString("STARTFROM", "");
		 
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
			while ((Utils.compareTime("00:00", content.get(iterator).getDepartureTime()) == -1) && (iterator < maxNumber - 1) && (iterator < content.size() - 1)) {
				++iterator;
			}
			while ((trainsAdded < maxNumber) && (iterator < content.size())) {
				BriefTrain curTrain = content.get(iterator);
				if (isFit(curTrain)) {					
					++trainsAdded;
					String mytime = new String();
					String myShort = new String();

					mytime = mytime.concat(curTrain.getDepartureTime());
					
					if (direction == TO) {
						if (STARTTO.equals("NOVO")) {
							myShort = myShort.concat("Новодачная ");
							myShort = myShort.concat("(" + curTrain.getDepartureTime() + ")");
						}
						if (STARTTO.equals("DOLG")) {
							myShort = myShort.concat("Долгопрудная ");
							myShort = myShort.concat("(" + curTrain.getDepartureTime() + ")");
						}
						if (STARTFROM.equals("TIMI")) {
							myShort = myShort.concat(" Тимирязевская ");
							myShort = myShort.concat("(" + curTrain.getArrivalTime() + ")");
						}
						if (STARTFROM.equals("SAVY")) {
							myShort = myShort.concat(" Савёловская ");
							myShort = myShort.concat("(" + curTrain.getArrivalTime() + ")");
						}					
					}
					if (direction == FROM) {
						if (STARTFROM.equals("TIMI")) {
							myShort = myShort.concat("Тимирязевская ");
							myShort = myShort.concat("(" + curTrain.getDepartureTime() + ")");
						}
						if (STARTFROM.equals("SAVY")) {
							myShort = myShort.concat("Савёловская ");
							myShort = myShort.concat("(" + curTrain.getDepartureTime() + ")");
						}	
						if (STARTTO.equals("NOVO")) {
							myShort = myShort.concat(" Новодачная ");
							myShort = myShort.concat("(" + curTrain.getArrivalTime() + ")");
						}
						if (STARTTO.equals("DOLG")) {
							myShort = myShort.concat(" Долгопрудная ");
							myShort = myShort.concat("(" + curTrain.getArrivalTime() + ")");
						}
					}
					FinalData data = new FinalData(myShort, Utils.getDifference(Utils.getCurrentTime(), mytime), null, curTrain.getLink());
					backContent.add(trainsAdded - 1, data);
				}
				++iterator;
			}
			return backContent;
		}	
	}
}
