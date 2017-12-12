package com.example.sajidsalman75.nearby.Model;

/**
 * Created by sajidsalman75 on 12/7/2017.
 */

public class Pictures {
    private int ID;
    byte[] PATH1, PATH2, PATH3, PATH4, PATH5;

    public Pictures(){
        this.ID = 0;
        this.PATH1 = null;
        this.PATH2 = null;
        this.PATH3 = null;
        this.PATH4 = null;
        this.PATH5 = null;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public byte[] getPATH1() {
        return PATH1;
    }

    public void setPATH1(byte[] PATH1) {
        this.PATH1 = PATH1;
    }

    public byte[] getPATH2() {
        return PATH2;
    }

    public void setPATH2(byte[] PATH2) {
        this.PATH2 = PATH2;
    }

    public byte[] getPATH3() {
        return PATH3;
    }

    public void setPATH3(byte[] PATH3) {
        this.PATH3 = PATH3;
    }

    public byte[] getPATH4() {
        return PATH4;
    }

    public void setPATH4(byte[] PATH4) {
        this.PATH4 = PATH4;
    }

    public byte[] getPATH5() {
        return PATH5;
    }

    public void setPATH5(byte[] PATH5) {
        this.PATH5 = PATH5;
    }
}
