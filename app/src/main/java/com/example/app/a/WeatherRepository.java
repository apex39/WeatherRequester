package com.example.app.a;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app.a.model.Period;
import com.example.app.a.model.WeatherResponse;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WeatherRepository {
    public static final int REQUEST_WEATHER = 1;
    public static final int RESPONSE_WEATHER = 2;
    public static final int AMOUNTS_OF_DAYS = 7;
    public static final String KEY_WEATHER_DATA = "weatherJson";
    private Messenger serviceMessenger = null;
    private boolean isBound = false;
    @Inject
    public WeatherRepository() {}

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    public LiveData<List<Period>> requestWeatherData() {
        MutableLiveData<List<Period>> liveData = new MutableLiveData<>();

        if (!isBound) return liveData;

        Message msg = Message.obtain(null, REQUEST_WEATHER);
        msg.replyTo = new Messenger(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == RESPONSE_WEATHER) {
                    String weatherJson = msg.getData().getString(KEY_WEATHER_DATA);
                    WeatherResponse response = new Gson().fromJson(weatherJson, WeatherResponse.class);
                    List<Period> periodList = response.getProperties().getPeriods().stream()
                            .filter(Period::isDaytime)
                            .limit(AMOUNTS_OF_DAYS)
                            .collect(Collectors.toList());

                    liveData.postValue(periodList);
                }
            }
        });

        try {
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            Log.e(this.getClass().getName(), Objects.requireNonNull(e.getMessage()));
        }

        return liveData;
    }

    public void bindService(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.app.b", "com.example.app.b.WeatherService"));
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        if (isBound) {
            context.unbindService(connection);
            isBound = false;
        }
    }
}
