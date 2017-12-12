package com.example.sajidsalman75.nearby.Model;

/**
 * Created by sajidsalman75 on 11/29/2017.
 */

public class Subcategory {
    private int ID, CATID;
    private String NAME;

    public Subcategory(String NAME) {
        this.NAME = NAME;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCATID() {
        return CATID;
    }

    public void setCATID(int CATID) {
        this.CATID = CATID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
