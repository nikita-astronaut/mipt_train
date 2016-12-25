package ru.rkarasev.miptrain;

import java.util.ArrayList;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.rkarasev.miptrain.utils.Train;
import android.content.Context;
import android.os.PowerManager;

public class FullInformationDownloadThread implements Runnable {
	  private ArrayList<String> links;
    private MainActivity context;
    private int direction;
    public final static int DAILY = 0;
    public final static int FERIAL = 1;
    public final static int WEEKEND = 2;
    public final static int EXCEPTIONAL = 3;
    public final static int INDEFINITE = 4;
    public final static int TO = 0;
    public final static int FROM = 1;
    public FullInformationDownloadThread(ArrayList<String> links, MainActivity context, int direction){
            this.links = links;
            this.context = context;
            this.direction = direction;
    }
    public void run() {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
            wl.acquire();
            try {
                  ArrayList<Train> trains = new ArrayList<Train>();
                  for (String link : links){
                  	Document trainDoc = Jsoup.connect("http://m.rasp.yandex.ru/" + link).timeout(5 * 1000).get();
                  	String trainNumber = trainDoc.select("div").first().text(); 
                  	trainNumber = trainNumber.replace("Пригородный поезд ", "");
                  	Elements stationElements = trainDoc.select("h4");    //Stations are encapsulated in <h4> elements
                  	Element stationsArray[] = new Element[stationElements.size()];
                  	stationElements.toArray(stationsArray);
                  	Hashtable<String, String> stations  = new Hashtable<String, String>();
                  	for (Element stationEl : stationsArray){
                          String name;
                          String time;
                          if (stationEl.html().contains("<br />")){
                          	String[] auxilaryStrings = stationEl.html().split("<br />");
                            	time = auxilaryStrings[1].substring(0, 5);
                          } else {
                              time = stationEl.text().substring(0, 5);
                          }
                          name = stationEl.select("a").first().text();
                          if (time.contains("-")){
                              time = "null";
                          }
                          stations.put(name, time);
                  	}
                  	if (trainNumber.charAt(0) == '6'){ 		
                  		trains.add(new Train(trainNumber, stations, direction, 0, null));
                  		System.out.println("trad");
                  	}
                  }
                  context.FullRefreshThreadListener(trains, direction);
            } catch (Throwable e) {
          	  context.FullRefreshThreadListener(null, direction);
            } finally {
          	  wl.release();
            }
    }
}