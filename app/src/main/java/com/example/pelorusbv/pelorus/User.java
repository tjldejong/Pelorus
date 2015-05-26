package com.example.pelorusbv.pelorus;

/**
 * Created by Tobias on 24-5-2015.
 */
public class User {
    private Long ID;

    private  static final User user = new User();

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public static User getInstance() {
        return user;
    }
}
