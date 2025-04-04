package com.example.app.a;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.app.a.databinding.ActivityMainBinding;
import com.example.app.a.model.DayWeather;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private WeatherViewModel weatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weatherViewModel.getWeatherData().observe(this, this::updateUI);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.requestWeatherForecastBtn.setOnClickListener(v -> {
            weatherViewModel.requestWeatherData();
            binding.progressBar.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        weatherViewModel.bindService(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        weatherViewModel.unbindService(getApplicationContext());
    }

    private void updateUI(List<DayWeather> dayWeatherList) {
        WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(dayWeatherList);
        binding.weatherRecyclerView.setAdapter(adapter);
        binding.weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.progressBar.setVisibility(View.GONE);
    }
}