package ru.rkarasev.miptrain.utils;
import java.util.Hashtable;


public class Train extends BriefTrain {
	public final static int TO = 0;
    public final static int FROM = 1;
	public final static int DAILY = 0;
    public final static int FERIAL = 1;
    public final static int WEEKEND = 2;
    public final static int EXCEPTIONAL = 3;
    public final static int INDEFINITE = 4;
    private int scheduleMode;
    private Hashtable<String, String> stations;
	private String exceptionDays;
	public Train(String number, Hashtable<String, String> stations, int direction, int scheduleMode, String exceptionDays){
		super(number,direction);
		this.stations = stations;
		this.scheduleMode = scheduleMode;
		this.exceptionDays = exceptionDays;
	}
	public Hashtable<String, String> getStations(){
		return stations;
	}
	public int getScheduleMode() {
		return scheduleMode;
	}
	public String getExceptionDays() {
		return exceptionDays;
	}
}
