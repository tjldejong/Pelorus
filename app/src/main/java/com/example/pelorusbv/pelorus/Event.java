package com.example.pelorusbv.pelorus;

/**
 * Created by Tobias on 11-6-2015.
 */
public class Event {
    private Long ID;

    private  static final Event event = new Event();

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public static Event getInstance() {
        return event;
    }
}
