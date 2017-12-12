package com.example.sajidsalman75.nearby.Model;

/**
 * Created by sajidsalman75 on 11/29/2017.
 */

public class Town {

    private int ID, CITYID;
    private String NAME;

    public Town(String NAME) {
        this.NAME = NAME;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCITYID() {
        return CITYID;
    }

    public void setCITYID(int CITYID) {
        this.CITYID = CITYID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
