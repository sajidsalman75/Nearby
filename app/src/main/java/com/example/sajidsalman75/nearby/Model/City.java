package com.example.sajidsalman75.nearby.Model;

/**
 * Created by sajidsalman75 on 11/29/2017.
 */

public class City {
    private int ID;
    private String NAME;

    public City(String NAME) {
        this.NAME = NAME;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
