package ru.rkarasev.miptrain.utils;

public class Station {
    private String time;
    private String name;
    public Station(String name, String time){
            this.time = time;
            this.name = name;
    }
    public String getTime(){
            return time;
    }
    public String getName(){
            return name;
    }
}
