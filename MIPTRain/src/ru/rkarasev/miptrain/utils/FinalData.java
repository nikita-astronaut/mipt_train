package ru.rkarasev.miptrain.utils;

public class FinalData {
	private String shortData;
	private String timeData;
	private String fullData;
	private String link;
	public FinalData(String timeData, String shortData, String fullData, String link) {
		this.shortData = shortData;
		this.link = link;
		this.timeData = timeData;
		this.fullData = fullData;
	}
	public String setShortData() {
		return shortData;
	}
	public String getTimeData() {
		return timeData;
	}
	public String setFullData() {
		return fullData;
	}
	public String getFullData() {
		return fullData;
	}
	public String getLink() {
		return link;
	}
	public void setTimeData(String timeData) {
		this.timeData = timeData;
	}
	public String getShortData() {
		return shortData;
	}
}
