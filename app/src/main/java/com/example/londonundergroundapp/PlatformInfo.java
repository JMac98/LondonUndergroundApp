package com.example.londonundergroundapp;

public class PlatformInfo {
    String platformName, destinationName, currentLocation;
    int timeToStation;

    public PlatformInfo(String platformName, String destinationName, String currentLocation, int timeToStation) {
        this.platformName = platformName;
        this.destinationName = destinationName;
        this.currentLocation = currentLocation;
        this.timeToStation = timeToStation;
    }

    public int length() {
        return this.destinationName.length();
    }

    public int getMinutesToStation() {
        return this.timeToStation / 60;
    }

    public int getSecondsToStation() {
        return this.timeToStation % 60;
    }
}

