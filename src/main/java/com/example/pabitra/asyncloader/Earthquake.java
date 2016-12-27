package com.example.pabitra.asyncloader;

import java.io.Serializable;

/**
 * Created by PABITRA on 11/13/2016.
 */
public class Earthquake implements Serializable {

    private String Magnitude;
    private String Place;
    private String Date;

    public Earthquake(String date, String magnitude, String place) {
        Date = date;
        Magnitude = magnitude;
        Place = place;
    }

    public String getDate() {
        return Date;
    }

    public String getMagnitude() {
        return Magnitude;
    }

    public String getPlace() {
        return Place;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "Date='" + Date + '\'' +
                ", Magnitude='" + Magnitude + '\'' +
                ", Place='" + Place + '\'' +
                '}';
    }
}
