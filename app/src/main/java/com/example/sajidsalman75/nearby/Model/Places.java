package com.example.sajidsalman75.nearby.Model;

/**
 * Created by sajidsalman75 on 11/10/2017.
 */

public class Places {
    private int ID;
    private int USERID;
    private int CATID;
    private int SUBCATID;
    private int OPENINGTIME, CLOSINGTIME, CITYID, TOWNID;
    private String NAME, ADDRESS;

    public Places(String NAME, int CITYID, int TOWNID, int CATID, int SUBCATID, String ADDRESS, int USERID) {
        this.NAME = NAME;
        this.CATID = CATID;
        this.CITYID = CITYID;
        this.ADDRESS = ADDRESS;
        this.USERID = USERID;
        this.SUBCATID = SUBCATID;
        this.TOWNID = TOWNID;
    }

    public int getCITYID() {
        return CITYID;
    }

    public void setCITYID(int CITYID) {
        this.CITYID = CITYID;
    }

    public int getTOWNID() {
        return TOWNID;
    }

    public void setTOWNID(int TOWNID) {
        this.TOWNID = TOWNID;
    }

    public int getOPENINGTIME() {
        return OPENINGTIME;
    }

    public void setOPENINGTIME(int OPENINGTIME) {
        this.OPENINGTIME = OPENINGTIME;
    }

    public int getCLOSINGTIME() {
        return CLOSINGTIME;
    }

    public void setCLOSINGTIME(int CLOSINGTIME) {
        this.CLOSINGTIME = CLOSINGTIME;
    }

    public int getCATID() {
        return CATID;
    }

    public void setCATID(int CATID) {
        this.CATID = CATID;
    }

    public int getSUBCATID() {
        return SUBCATID;
    }

    public void setSUBCATID(int SUBCATID) {
        this.SUBCATID = SUBCATID;
    }

    public int getUSERID() {
        return USERID;
    }

    public void setUSERID(int USERID) {
        this.USERID = USERID;
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

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }
}