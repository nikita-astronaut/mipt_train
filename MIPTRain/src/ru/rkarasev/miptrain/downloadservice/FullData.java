package ru.rkarasev.miptrain.downloadservice;

import java.util.ArrayList;

import ru.rkarasev.miptrain.utils.Train;

public class FullData {
	private ArrayList<Train> toTrains;
	private ArrayList<Train> fromTrains;
	private String date;
	public FullData(ArrayList<Train> toTrains, ArrayList<Train> fromTrains, String date){
		this.toTrains = toTrains;
		this.fromTrains = fromTrains;
		this.date = date;
	}
	public ArrayList<Train> getToTrains(){
		return toTrains;
	}
	public ArrayList<Train> getFromTrains(){
		return fromTrains;
	}
	public String getDate(){
		return date;
	}
}
