package com.example.mostafa.movieapp;

/**
 * Created by Mostafa on 3/6/2017.
 */

public class TrailerDetails {
    private String TrailerName;
    private String Trailerkey;
    public TrailerDetails(String key, String name) {
        this.Trailerkey = key;
        TrailerName = name;
    }
    public String getName() {
        return TrailerName;
    }

    public String getKey() {
        return Trailerkey;
    }

}
