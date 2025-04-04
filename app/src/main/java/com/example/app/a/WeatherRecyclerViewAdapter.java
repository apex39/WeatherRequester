package com.example.app.a;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.a.model.DayWeather;

import java.util.List;

class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder> {

    private final List<DayWeather> weatherList;

    public WeatherRecyclerViewAdapter(List<DayWeather> weatherList) {
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DayWeather current = weatherList.get(position);

        holder.dateTextView.setText(current.getDate());
        holder.tempTextView.setText(current.getTemperature());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView tempTextView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
        }
    }
}