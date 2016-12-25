package ru.rkarasev.miptrain.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
	public static boolean isWeekEnd() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();
		String dayOfTheWeek = sdf.format(d);
		if ((dayOfTheWeek.equals("суббота")) || (dayOfTheWeek.equals("воскресенье")) || (dayOfTheWeek.equals("Sunday")) || (dayOfTheWeek.equals("Saturday"))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static int compareTime(String time1, String time2) {
		// 1 if time1 > time 2
		try {
			int hour1 = Integer.valueOf(time1.substring(0, 2));
			int minute1 = hour1 * 60 + Integer.valueOf(time1.substring(3));
			int hour2 = Integer.valueOf(time2.substring(0, 2));
			int minute2 = hour2 * 60 + Integer.valueOf(time2.substring(3));
		if (minute1 < 180){
			if (minute2 < 180){
				if (minute1 < minute2){
					return -1;
				} else {
					if (minute1 > minute2){
						return 1;
					} else {
						return 0;
					}
				}
			} else {
				return 1;
			}
		} else {
			if (minute2 < 180){
				return -1;
			} else {
				if (minute1 < minute2){
					return -1;
				} else {
					if (minute1 > minute2){
						return 1;
					} else {
						return 0;
					}
				}
			}
		}
		} catch(Exception e) {
			return -1;
		}
	}
	
	public static String getCurrentTime(){
		Calendar cal = new GregorianCalendar();
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		if (hour.length() < 2) {
			hour = "0" + hour;
		}
		if (minute.length() < 2) {
			minute = "0" + minute;
		}
		return hour + ":" + minute;
	}
	
	public static String getCurrentDate(){
		  Calendar cal1 = Calendar.getInstance();
		  return (cal1.get(Calendar.YEAR) + "-" + (cal1.get(Calendar.MONTH) + 1) + "-" + cal1.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String remainingTimeFormat(String time) {
		String remainingTimeFormat = new String();
		if (time.substring(0, 2).equals("00") == false) {
			if (Integer.valueOf(time.substring(0, 2)) < 10) {
				remainingTimeFormat = remainingTimeFormat.concat(time.substring(1, 2));
			} else {
				remainingTimeFormat = remainingTimeFormat.concat(time.substring(0, 2));
			}	
			remainingTimeFormat = remainingTimeFormat.concat(" час.\n");
		}
		
		if (Integer.valueOf(time.substring(3)) < 10) {
			remainingTimeFormat = remainingTimeFormat.concat(time.substring(4));
		} else {
			remainingTimeFormat = remainingTimeFormat.concat(time.substring(3));
		}
		remainingTimeFormat = remainingTimeFormat.concat(" мин."); //non-breaking space, then usual space
		
		return remainingTimeFormat;
	}
	
	public static String getDifference(String realTime, String trainTime) {
		System.out.println(realTime);
		int realHour = Integer.valueOf(realTime.substring(0, 2));
		int realMinute = realHour * 60 + Integer.valueOf(realTime.substring(3));
		
		int trainHour = Integer.valueOf(trainTime.substring(0, 2));
		if (trainHour < 3) {
			trainHour += 24;
		}
		int trainMunite = trainHour * 60 + Integer.valueOf(trainTime.substring(3));
		int difference = trainMunite - realMinute;
		String returnString = new String();
		if (difference / 60 > 9) {
			returnString = returnString.concat(Integer.toString(difference / 60));
		} else {
			returnString = returnString.concat("0" + Integer.toString(difference / 60));
		}
		if (difference % 60 > 9) {
			returnString = returnString.concat(":" + Integer.toString(difference % 60));
		} else {
			returnString = returnString.concat(":0" + Integer.toString(difference % 60));
		}
		return returnString;
	}
	public static boolean isToday(String exeptionDays){
		String[] days = exeptionDays.split(" ");
		String currentDay = getCurrentDate().substring(5);
		boolean isToday = false;
		for(String day : days){
			if(day.equals(currentDay)){
				isToday = true;
			}
		}
		return isToday;
	}
	public static String updatedOutput(String whenUpdated){
		try{
			System.out.println("whenUpdated" + whenUpdated);
		GregorianCalendar today = new GregorianCalendar();
		String[] arr = whenUpdated.split("-");
		GregorianCalendar updated = new GregorianCalendar(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]) - 1, Integer.valueOf(arr[2]));
		System.out.println("today" + today.get(Calendar.YEAR) + "-" +  today.get(Calendar.MONTH) + "-" + today.get(Calendar.DAY_OF_MONTH));
		long diff = today.getTimeInMillis() - updated.getTimeInMillis();
		int days = (int)(diff/(24 * 3600 * 1000));
		if(days == 0){
			return "База данных была обновлена сегодня";
		}
		if((days % 10 == 1) && (days % 100 != 11)){
			return "База данных была обновлена " + days + " день назад";
		}
		if(       (  ((days % 10) == 2 ) || ((days % 10) == 3 ) || ((days % 10) == 4 )  ) && (  ((days % 100) < 10) || ((days % 100) > 20)  )     ){
			return "База данных была обновлена " + days + " дня назад";
		}
		return "База данных была обновлена " + days + " дней назад";
		} catch (Throwable e){
			return null;
		}
	}
}
