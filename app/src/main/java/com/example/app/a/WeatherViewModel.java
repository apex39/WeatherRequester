package com.example.app.a;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.app.a.model.DayWeather;
import com.example.app.a.model.Period;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<List<Period>> weatherData = new MutableLiveData<>();
    private final WeatherRepository repository;

    @Inject
    public WeatherViewModel(WeatherRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<DayWeather>> getWeatherData() {
        return Transformations.map(weatherData, periods -> {
            if (periods == null) {
                return Collections.emptyList();
            }

            return periods.stream()
                    .filter(Period::isDaytime)
                    .limit(7)
                    .map(period -> new DayWeather(
                            formatWeatherDate(period.getStartTime()),
                            fahrenheitToCelsiusString(period.getTemperature())
                    ))
                    .collect(Collectors.toList());
        });
    }
    public void requestWeatherData() {
        repository.requestWeatherData().observeForever(weatherData::postValue);
    }


    public void bindService(Context context) {
        repository.bindService(context);
    }

    public void unbindService(Context context) {
        repository.unbindService(context);
    }

    private String fahrenheitToCelsiusString(int fahrenheit) {
        int celsius = (int) Math.round((fahrenheit - 32) * 5.0 / 9);
        return String.format(Locale.getDefault(), "%d°C", celsius);
    }

    private String formatWeatherDate(String isoDate) {
        ZonedDateTime dateTime = ZonedDateTime.parse(isoDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy EEEE", Locale.GERMAN);
        return dateTime.format(formatter);
    }
}

