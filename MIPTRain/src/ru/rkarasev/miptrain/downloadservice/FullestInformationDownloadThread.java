package ru.rkarasev.miptrain.downloadservice;

import java.util.ArrayList;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.rkarasev.miptrain.utils.Train;
import ru.rkarasev.miptrain.utils.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FullestInformationDownloadThread implements Runnable {
    private Context context;
    public final static int DAILY = 0;
    public final static int FERIAL = 1;
    public final static int WEEKEND = 2;
    public final static int EXCEPTIONAL = 3;
    public final static int INDEFINITE = 4;
    public final static int TO = 0;
    public final static int FROM = 1;
    public FullestInformationDownloadThread(Context context){
            this.context = context;
    }
    public void run() {
    	try{
    		String when = Utils.getCurrentDate();
  		    SharedPreferences startPref = context.getSharedPreferences("MyPref", 0);
  		    startPref.edit().putString("REFRESHED", when).commit();	    
    		String fromName = "Долгопрудная";
    		String toName = "Москва (Савёловский вокзал)";
    		int direction = TO;
    		ArrayList<Train> toTrains = Download(when, fromName, toName, direction);
    		fromName = "Москва (Савёловский вокзал)";
    		toName = "Долгопрудная";
    		direction = FROM;
    		ArrayList<Train> fromTrains = Download(when, fromName, toName, direction);
    		FullData fullData = new FullData(toTrains, fromTrains, when);
    		handleDB(fullData);
    		System.out.println("Обновление базы данных завершено");
    	} catch(Throwable e){
    		System.out.println("Полная заргузка невозможна");
    		e.printStackTrace();
    	} finally {
    		DownloadService.updated();
    	}
    } 
    public void handleDB(FullData fullData) throws DateBaseException{
    	try{
		  TrainsDBManager dbManager = new TrainsDBManager(context);
		  if (Utils.isWeekEnd()) {
			  dbManager.deleteData(0);
			  dbManager.deleteData(2);
			  dbManager.deleteData(3);
			  dbManager.deleteData(4);
			  System.out.println("deleted 0 and 2");
		  } else {
			  dbManager.deleteData(0);
			  dbManager.deleteData(1);
			  dbManager.deleteData(3);
			  dbManager.deleteData(4);
			  System.out.println("deleted 0 and 1");
		  }
		  ArrayList<Train> trains = fullData.getToTrains();
		  trains.addAll(fullData.getFromTrains()); 		  
		  
		  dbManager.insertData(trains); 
    	} catch(Exception e){
    		System.out.println("DB Exception");
    		e.printStackTrace();
    		throw new DateBaseException();
    	}
	 }
    private ArrayList<Train> Download(String when, String fromName, String toName, int direction){
    	Document doc;
        try{
      	    doc = Jsoup.connect("http://m.rasp.yandex.ru/search/")      //Parsing starts here... All the facts about the information location
                              .data("fromName", fromName)			  //are empiric and therefore not reliable
                              .data("fromId", "")
                              .data("toName", toName)
                              .data("toId", "")
                              .data("when", when)
                              .data("when_interval", "6")           //six means "at any time"
                              .data("search_type", "suburban")
                              .data("show_gones", "yes")
                              .timeout(0) 
                              .get();
              Elements trainElements = doc.select("li");           //Trains are encapsulated in <li> elements
              Element trainsArray[] = new Element[trainElements.size()];
              trainElements.toArray(trainsArray);
              ArrayList<Train> trains = new ArrayList<Train>();
              for(Element trainEl : trainsArray){
              	String link = trainEl.select("div > a").first().attr("href");     //Each train element contains a link to a list of its stations
              	String trainNumber = trainEl.select("div > a").first().text();  //<a> element contain the number of the train
              	String timeStr = trainEl.select("div > b").first().text();
              	String departureTime = timeStr.substring(0, 5);
              	if(Utils.compareTime(departureTime, "00:00") == -1){
              	System.out.println(departureTime + "is before 03:00");
              	Document trainDoc = Jsoup.connect("http://m.rasp.yandex.ru/" + link).timeout(0).get(); //Parsing train information
              	String scheduleModeString = trainDoc.select("div").get(1).text();      //Getting schedule mode information
              	int scheduleMode;
              	String exceptionDays = null;
              	System.out.println(scheduleModeString);
              	if(scheduleModeString.substring(0,9).equals("Ежедневно")){
              		scheduleMode = DAILY;
              	} else if(scheduleModeString.substring(0,9).equals("По будням")){
              		scheduleMode = FERIAL;
              	} else if(scheduleModeString.substring(0,11).equals("По выходным")){
              		scheduleMode = WEEKEND;
              	} else if(scheduleModeString.substring(0, 6).equals("Только")){
              		//It's an exceptional train
              		try{
              			scheduleModeString = scheduleModeString.replace("Только", "");
              			scheduleModeString = scheduleModeString.replace(" ", "");
              			String[] pseudoDays = scheduleModeString.split(",");
              			exceptionDays = "";
              			ArrayList<String> days = new ArrayList<String>();
              			for(String pseudoDay : pseudoDays){
              				if(pseudoDay.length() > 2){
              					//On my design it contains month
              					String mon;
              					String month;
              					String dayOfMonth;
              					if((pseudoDay.charAt(1) >= '0') && (pseudoDay.charAt(1) <= '9')){
                						month = pseudoDay.substring(2);
                						dayOfMonth = pseudoDay.substring(0, 2);
                					} else {
                						month = pseudoDay.substring(1);
                						dayOfMonth = "0" + pseudoDay.substring(0, 1);
                					}
              						System.out.println(month);
                					month = month.substring(1);
                					System.out.println(month);
                					if("январь".contains(month)){
                						mon = "01";
                					} else if("февраля".contains(month)){
                						mon = "02";
                					} else if("марта".contains(month)){
                						mon = "03";
                					} else if("апреля".contains(month)){
                						mon = "04";
                					} else if("мая".contains(month)){
                						mon = "05";
                					} else if("июня".contains(month)){
                						mon = "06";
                					} else if("июля".contains(month)){
                						mon = "07";
                					} else if("августа".contains(month)){
                						mon = "08";
                					} else if("сентября".contains(month)){
                						mon = "09";
                					} else if("октября".contains(month)){
                						mon = "10";
                					} else if("ноября".contains(month)){
                						mon = "11";
                					} else if("декабря".contains(month)){
                						mon = "12";
                					} else {
                						//Shit happens
                						throw new Exception();
                					}
              					for(String day : days){
              						exceptionDays = exceptionDays.concat(" " + mon + "-" + day);
              					}
              					days.clear();
              					exceptionDays.concat("" + mon + "-" + dayOfMonth);
              				} else{
              					if(pseudoDay.length() > 1){
              						days.add(pseudoDay);
              					} else {
              						days.add("0" + pseudoDay);
              					}
              				}
              			}
              			scheduleMode = EXCEPTIONAL;
              		} catch (Exception e){
              		//It wasn't what I expected
              			e.printStackTrace();
              			scheduleMode = INDEFINITE;
              		}
              	} else {
              		scheduleMode = INDEFINITE;
              	}
              	// Here we have accomplished exception days list
              	Elements stationElements = trainDoc.select("h4");    //Stations are encapsulated in <h4> elements
              	Element stationsArray[] = new Element[stationElements.size()];
              	stationElements.toArray(stationsArray);
              	Hashtable<String, String> stations  = new Hashtable<String, String>();
              	for(Element stationEl : stationsArray){
                      String name;
                      String time;
                      if (stationEl.html().contains("<br />")){
                      	String[] auxilaryStrings = stationEl.html().split("<br />");
                        	time = auxilaryStrings[1].substring(0, 5);
                      } else {
                          time = stationEl.text().substring(0, 5);
                      }
                      name = stationEl.select("a").first().text();
                      if(time.contains("-")){
                          time = "null";
                      }
                      stations.put(name, time);
              	}
              	if(trainNumber.charAt(0) == '6'){ 						//It's a hazardous way to check if the train is an express or not
              		trains.add(new Train(trainNumber, stations, direction, scheduleMode, exceptionDays));
              		System.out.println("Train added");
              	}
              	if(exceptionDays != null){
              		System.out.println(exceptionDays);
              	}
              } else {
            	  
              }
              	
              }
              return trains;
        } catch(Exception e){
        	e.printStackTrace();
        	return null;
        }
    }
}
