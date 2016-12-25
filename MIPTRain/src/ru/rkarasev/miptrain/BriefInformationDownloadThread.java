package ru.rkarasev.miptrain;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.rkarasev.miptrain.utils.BriefTrain;

public class BriefInformationDownloadThread implements Runnable {
    private String when ;
    private String fromName ;
    private String toName ;
    private MainActivity context;
    private int direction;
    public final static int TO = 0;
    public final static int FROM = 1;
    public BriefInformationDownloadThread(String when, String fromName, String toName, int direction, MainActivity context){
            this.when = when;
            this.fromName = fromName;
            this.toName = toName;
            this.direction = direction;
            this.context = context;
    }
    public void run() {
          
            Document doc;
            try{
          	  doc = Jsoup.connect("http://m.rasp.yandex.ru/search/")      //Parsing starts here... 
                                  .data("fromName", fromName)			  
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
                  	ArrayList<BriefTrain> trains = new ArrayList<BriefTrain>();
                  	for(Element trainEl : trainsArray){
                  		String link = trainEl.select("div > a").first().attr("href");
                  		String trainNumber = trainEl.select("div > a").first().text();  //<a> element contain the number of the train
                  		String timeStr = trainEl.select("div > b").first().text();
        	  				String departureTime = timeStr.substring(0, 5);
        	  				String arrivalTime = timeStr.substring(timeStr.length() - 5, timeStr.length());  
                  		if(trainNumber.charAt(0) == '6'){
                  			trains.add(new BriefTrain(trainNumber, direction, departureTime, arrivalTime, link));
                  		}
                  	}
                  	context.BriefRefreshThreadListener(trains, direction);

                  		//Some handling method must be here
              } catch (Throwable e) {
              	//Unable to connect
              	context.BriefRefreshThreadListener(null, direction);
              }
    	} 
}