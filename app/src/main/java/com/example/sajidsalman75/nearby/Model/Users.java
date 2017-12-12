package com.example.sajidsalman75.nearby.Model;

/**
 * Created by sajidsalman75 on 11/10/2017.
 */

public class Users {
    private int ID;
    private String FIRSTNAME, LASTNAME, PASSWORD, EMAIL, ROLE;

    public Users(String FIRSTNAME, String LASTNAME, String EMAIL, String PASSWORD, String ROLE) {
        this.FIRSTNAME = FIRSTNAME;
        this.LASTNAME = LASTNAME;
        this.PASSWORD = PASSWORD;
        this.EMAIL = EMAIL;
        this.ROLE = ROLE;
    }

    public Users(){
        int ID = 0;
        this.FIRSTNAME = null;
        this.LASTNAME = null;
        this.PASSWORD = null;
        this.EMAIL = null;
        this.ROLE = null;
    }

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFIRSTNAME() {
        return FIRSTNAME;
    }

    public void setFIRSTNAME(String FIRSTNAME) {
        this.FIRSTNAME = FIRSTNAME;
    }

    public String getLASTNAME() {
        return LASTNAME;
    }

    public void setLASTNAME(String LASTNAME) {
        this.LASTNAME = LASTNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
