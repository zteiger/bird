package com.example.zachteiger.finalassessment;

import java.util.Date;

public class Bird {


    public String birdZip;
    public String birdName;
    public long birdDate;
    public int birdImportance;
    public String userName;


    public Bird(){

    }

    public Bird(String birdZip, String birdName, long birdDate, int  birdImportance, String userName){
        this.birdName = birdName;
        this.birdDate = birdDate;
        this.birdZip = birdZip;
        this.birdImportance = birdImportance;
        this.userName = userName;

    }


}
