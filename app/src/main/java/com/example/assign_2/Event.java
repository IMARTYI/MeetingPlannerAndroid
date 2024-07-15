
package com.example.assign_2;

public class Event {

    int ID;
    String eventName;
    String date;
    public String time;
    public String Description;
    String Contact;

    public Event (String name ,String date, String time , String Description,String contact){

        this.eventName = name;
        this.date = date;
        this.time = time;
        this.Description = Description;
        this.Contact = contact;
    }

    /**
     * Getters for the Event Class
     */
    public String getContact() {
        return Contact;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDate() {
        return date;
    }
    public String getDescription() {
        return Description;
    }

    public String getTime() {
        return time;
    }

}
