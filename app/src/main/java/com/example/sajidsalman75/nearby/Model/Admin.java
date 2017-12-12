package com.example.sajidsalman75.nearby.Model;

/**
 * Created by sajidsalman75 on 11/9/2017.
 */

public class Admin {
    private int ID;
    private String FIRSTNAME, LASTNAME, PASSWORD, EMAIL;

    public Admin(String FIRSTNAME, String LASTNAME, String PASSWORD, String EMAIL) {
        this.FIRSTNAME = FIRSTNAME;
        this.LASTNAME = LASTNAME;
        this.PASSWORD = PASSWORD;
        this.EMAIL = EMAIL;
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
