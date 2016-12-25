package ru.rkarasev.miptrain.utils;

public class BriefTrain {
	
    public final static int TO = 0;
    public final static int FROM = 1;
	protected String number;
	protected int direction;
	protected String departureTime;
	protected String arrivalTime;
	private String link;
	public BriefTrain(String number, int direction){
		this.number = number;
		this.direction = direction;
	}
	public BriefTrain(String number, int direction, String departureTime, String arrivalTime, String link){
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.number = number;
		this.direction = direction;
		this.link = link;
	}
	public int getDirection(){
		return direction;
	}
	public String getDepartureTime(){
		return departureTime;
	}
	public String getNumber(){
		return number;
	}
	public String getArrivalTime(){
		return arrivalTime;
	}
	public String getLink(){
		return link;
	}
	
}
