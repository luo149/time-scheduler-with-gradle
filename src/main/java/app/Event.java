package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.util.ArrayList;

public class Event{

    private String title;
    private String location;
    private ArrayList<String> timeSlots;
    private ArrayList<ArrayList<String>> participants;

    /*********************************************************************
     *                              Constructor
     *********************************************************************/

    public Event(String title, String location, ArrayList<String> timeSlots, ArrayList<ArrayList<String>> participants){
        this.title = title;
        this.location = location;
        this.timeSlots = timeSlots;
        this.participants = participants;
    }

    /*********************************************************************
     *                             Getters
     *********************************************************************/
    public String getTitle(){
        return title;
    }

    public  String getLocation(){
        return location;
    }

    public  ArrayList<String> getTimeSlots(){
        return timeSlots;
    }

    public  ArrayList<ArrayList<String>> getParticipants(){
        return participants;
    }

    /*********************************************************************
     *                              Setters
     *********************************************************************/

    public  void setTitle(String title){
        this.title = title;
    }

    public  void setLocation(String location){
        this.location = location;
    }

    public  void setTimeSlots(ArrayList<String> timeSlots){
        this.timeSlots = timeSlots;
    }

    public void setParticipants(ArrayList<ArrayList<String>> participants){
        this.participants = participants;
    }

}
