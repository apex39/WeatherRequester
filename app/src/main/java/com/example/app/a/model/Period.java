package com.example.app.a.model;

import java.io.Serializable;

public class Period implements Serializable {
    private final String startTime;
    private final boolean isDaytime;
    private final int temperature;

    public Period(String startTime, boolean isDaytime, int temperature) {
        this.startTime = startTime;
        this.isDaytime = isDaytime;
        this.temperature = temperature;
    }

    public String getStartTime() {
        return startTime;
    }

    public boolean isDaytime() {
        return isDaytime;
    }

    public int getTemperature() {
        return temperature;
    }
}
