package com.example.app.a.model;

public class DayWeather {
    private final String date;
    private final String temperature;

    public DayWeather(String date, String temperature) {
        this.date = date;
        this.temperature = temperature;
    }
    public String getDate() { return date; }
    public String getTemperature() { return temperature; }

}