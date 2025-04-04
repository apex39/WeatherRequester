package com.example.app.a.model;

import java.io.Serializable;

public class WeatherResponse implements Serializable {
    private final Properties properties;

    public WeatherResponse(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}