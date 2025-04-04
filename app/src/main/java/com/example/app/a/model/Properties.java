package com.example.app.a.model;

import java.io.Serializable;
import java.util.List;

public class Properties implements Serializable {
    private final List<Period> periods;

    public Properties(List<com.example.app.a.model.Period> periods) {
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

}